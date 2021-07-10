package com.example.redhood.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.redhood.database.RepositoryCallback;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;
import com.example.redhood.dialogs.ChooseSetDialog;
import com.example.redhood.translation.MyClickableSpan;
import com.example.redhood.R;
import com.example.redhood.translation.Translate;
import com.example.redhood.viewmodels.HomeViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;

public class RecognitionFragment extends Fragment implements View.OnClickListener{

    private ImageView imageView;
    private TextView myTextView;
    private ArrayList<String> words= new ArrayList<>();
    private RelativeLayout layout;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private String currentPhotoPath;
    private Snackbar mySnackbar;
    private static HomeViewModel homeViewModel;
    private static CharSequence[] setsTitles = {};
    private static List<Set> setsObjects = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_recognition_fragment, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        myTextView = view.findViewById(R.id.textView);
        imageView = view.findViewById(R.id.imageView);
        view.findViewById(R.id.checkText).setOnClickListener(this);
        view.findViewById(R.id.select_image).setOnClickListener(this);
        layout = view.findViewById(R.id.relativeLayout);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkText:
                if (imageBitmap != null) {
                    runTextRecognition();
                }
                break;
            case R.id.select_image:
                dispatchTakePictureIntent();
                myTextView.setText("");
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.redhood",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    public void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
        imageBitmap = bitmap;
    }

    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                processExtractedText(texts);
            }
        }).addOnFailureListener(exception -> Toast.makeText(getContext(),
                "Exception", Toast.LENGTH_LONG).show());
    }

    //Loading set names from DB before opening AddNewWordDialog
    public void preloadData(String orig, String trans){
        homeViewModel.getAllSets().observe(getViewLifecycleOwner(), sets -> {
            setsObjects = sets;
            setsTitles = new CharSequence[sets.size()];
            for(int i=0; i<sets.size(); i++){
                setsTitles[i] = sets.get(i).getName();
            }
            if(setsTitles.length>0) openDialog(orig, trans);
            else Toast.makeText(getActivity(), "First create a set!", Toast.LENGTH_SHORT).show();
        });
    }

    //Opening AddNewWordDialog
    public void openDialog(String original, String translation){
        //onChanged can be called from multiple states causing openDialog to fire up
        //So we need to check whether the current fragment is added
        if (!isAdded()) return;
        homeViewModel.getAllSets().removeObservers(getViewLifecycleOwner());
        ChooseSetDialog dialog = new ChooseSetDialog(setsTitles);
        dialog.show(getChildFragmentManager(), "choose_set_dialog");
        dialog.setOnSelectedListener(choice -> {
            Word word = new Word(setsObjects.get(choice).getId(), original, translation);
            homeViewModel.insertWord(word);
        });
    }

    private void addClickableText(SpannableStringBuilder ssb, int startPos, String clickableText) {
        ssb.append(clickableText);
        MyClickableSpan clickableSpan = new MyClickableSpan(clickableText, startPos, ssb.length());

        clickableSpan.setOnSpanClickListener((start, end, selected, original) -> {
            if(!selected){
                Context context = getContext();
                homeViewModel.getTranslation(original, result -> {
                    mySnackbar = Snackbar.make(layout, original+" - "+result, BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.setAction("ADD", v ->{
                        preloadData(original, result);
                        mySnackbar.dismiss();
                    });
                    mySnackbar.show();
                }, getContext());
                ssb.setSpan(new BackgroundColorSpan(ContextCompat.getColor(getActivity(), R.color.pink)), start, end,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            }else{
                ssb.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        clickableSpan.setSelected(!selected);
        myTextView.setText(ssb);
        });

        ssb.setSpan(clickableSpan, startPos, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void processExtractedText(FirebaseVisionText firebaseVisionText) {
        myTextView.setText(null);
        if (firebaseVisionText.getBlocks().size() == 0) {
            myTextView.setText(R.string.no_text);
            return;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
            for(FirebaseVisionText.Line line : block.getLines()){
                for(FirebaseVisionText.Element word : line.getElements()){
                    addClickableText(ssb, ssb.length(), word.getText());
                    ssb.append(" ");
                }
            }
        }
        ssb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        myTextView.setMovementMethod(LinkMovementMethod.getInstance());
        myTextView.setText(ssb);
    }

}
