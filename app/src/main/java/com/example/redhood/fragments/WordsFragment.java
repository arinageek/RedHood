package com.example.redhood.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redhood.MainActivity;
import com.example.redhood.R;
import com.example.redhood.SetAdapter;
import com.example.redhood.WordAdapter;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;
import com.example.redhood.dialogs.AddNewSetDialog;
import com.example.redhood.viewmodels.SetViewModel;
import com.example.redhood.database.entities.Set;
import com.example.redhood.viewmodels.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WordsFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static WordViewModel wordViewModel;
    private static WordAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_words, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewWords);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WordAdapter();
        recyclerView.setAdapter(adapter);

        int setId = Integer.parseInt(getArguments().getString("set_id", "0"));

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        wordViewModel.getSetWithWords(setId).observe(getActivity(), setWithWords -> {
            if(setWithWords.isEmpty()) return;
            //update RecyclerView
            adapter.submitList(setWithWords.get(0).words);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Do you want to delete this word?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                wordViewModel.deleteWord(adapter.getWordAt(viewHolder.getAdapterPosition()));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        }).create().show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.wordOnItemClickListener((WordAdapter.OnItemClickListener) word -> {
            //navigate to home page just for trial
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        });


        return rootView;
    }

    /*
    public void openAddNewSetDialog(){
        AddNewSetDialog addNewSetDialog = new AddNewSetDialog();
        addNewSetDialog.show(getChildFragmentManager(), "set dialog");
    }

    public static void applyTexts(String title) {
        wordViewModel.insertWord();
    }*/
}
