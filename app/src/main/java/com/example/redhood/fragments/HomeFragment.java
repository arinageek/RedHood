package com.example.redhood.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.redhood.database.SetRepository;
import com.example.redhood.database.entities.Set;
import com.example.redhood.dialogs.ChooseSetDialog;
import com.example.redhood.viewmodels.HomeViewModel;
import com.example.redhood.R;
import com.example.redhood.database.entities.Word;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static EditText etOrigWord;
    private static EditText etTransWord;
    private Button addBtn;

    private static HomeViewModel homeViewModel;
    private static String original="";
    private static String translation="";

    //Just titles of sets to send to ChooseSetDialog
    private static CharSequence[] setsTitles;
    //Array of Set Objects to get a setId by index
    private static List<Set> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etOrigWord = view.findViewById(R.id.et_origWord);
        etTransWord = view.findViewById(R.id.et_transWord);
        addBtn = view.findViewById(R.id.addBtn);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                original = etOrigWord.getText().toString();
                translation = etTransWord.getText().toString();
                preloadData();
            }
        });

        return view;
    }

    //This function is called from ChooseSetDialog
    public static CharSequence[] getSets(){
        return setsTitles;
    }

    /*
    Load sets from Room Db
    Save set objects into "items" array
    Save just titles (for the dialog) into "setsTitles" array
    */
    public void preloadData(){
        homeViewModel.getAllSets().observe(getActivity(), new Observer<List<Set>>() {
            @Override
            public void onChanged(List<Set> sets) {
                items = sets;
                setsTitles = new CharSequence[sets.size()];
                for(int i=0; i<sets.size(); i++){
                    setsTitles[i] = sets.get(i).getName();
                }
                openDialog();
            }
        });
    }

    public void openDialog(){
        ChooseSetDialog dialog = new ChooseSetDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "choose_set_dialog");
    }

    //This function is called from ChooseSetDialog
    public static void saveWordToSet(int choice){
        Word word = new Word(items.get(choice).getId(), original, translation);
        original=""; translation="";
        homeViewModel.insertWord(word);
        etOrigWord.setText("");
        etTransWord.setText("");
    }
}
