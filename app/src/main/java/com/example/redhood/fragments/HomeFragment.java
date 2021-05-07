package com.example.redhood.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.redhood.MainActivity;
import com.example.redhood.database.SetRepository;
import com.example.redhood.database.entities.Set;
import com.example.redhood.dialogs.ChooseSetDialog;
import com.example.redhood.viewmodels.HomeViewModel;
import com.example.redhood.R;
import com.example.redhood.database.entities.Word;
import com.example.redhood.viewmodels.SetViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static EditText etOrigWord;
    private static EditText etTransWord;
    private Button addBtn;
    private Button cameraBtn;

    private static HomeViewModel homeViewModel;
    private static String original="";
    private static String translation="";

    private static CharSequence[] setsTitles = {};
    private static List<Set> setsObjects = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etOrigWord = view.findViewById(R.id.et_origWord);
        etTransWord = view.findViewById(R.id.et_transWord);
        addBtn = view.findViewById(R.id.addBtn);
        cameraBtn = view.findViewById(R.id.cameraBtn);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        addBtn.setOnClickListener(v -> {
            original = etOrigWord.getText().toString();
            translation = etTransWord.getText().toString();
            if(original.trim().isEmpty() || translation.trim().isEmpty()){
                Toast.makeText(getActivity(), "Make sure that the fields aren't empty!", Toast.LENGTH_SHORT).show();
            }else{
                preloadData();
            }
        });

        cameraBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RecognitionFragment(), "recognition").addToBackStack(null).commit();
        });

        return view;
    }

    /*
    Load sets from Room Db
    Save set objects into "items" array
    Save just titles (for the dialog) into "setsTitles" array
    */
    public void preloadData(){
        homeViewModel.getAllSets().observe(getViewLifecycleOwner(), sets -> {
            setsObjects = sets;
            setsTitles = new CharSequence[sets.size()];
            for(int i=0; i<sets.size(); i++){
                setsTitles[i] = sets.get(i).getName();
            }
            if(setsTitles.length>0){
                openDialog();
            }else{
                Toast.makeText(getActivity(), "First create a set!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openDialog(){
        //onChanged can be called from multiple states causing openDialog to fire up
        //So we need to check whether the current fragment is added
        if (!isAdded()) return;
        ChooseSetDialog dialog = new ChooseSetDialog(setsTitles);
        dialog.show(getChildFragmentManager(), "choose_set_dialog");
        dialog.setOnSelectedListener(choice -> {
            Word word = new Word(setsObjects.get(choice).getId(), original, translation);
            homeViewModel.insertWord(word);
            original=""; translation="";
            etOrigWord.setText(""); etTransWord.setText("");
        });
    }
}
