package com.example.redhood.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redhood.R;
import com.example.redhood.adapters.WordAdapter;
import com.example.redhood.database.entities.Word;
import com.example.redhood.dialogs.AddNewWordDialog;
import com.example.redhood.viewmodels.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class WordsFragment extends Fragment{

    private static RecyclerView recyclerView;
    private static WordViewModel wordViewModel;
    private static WordAdapter adapter;
    private FloatingActionButton fab;
    private static int setId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_words, container, false);

        fab = rootView.findViewById(R.id.fab);
        recyclerView = rootView.findViewById(R.id.recyclerViewWords);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WordAdapter();
        recyclerView.setAdapter(adapter);

        setId = Integer.parseInt(getArguments().getString("set_id", "0"));

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
                        .setPositiveButton("Delete", (dialog, which) -> wordViewModel.deleteWord(adapter.getWordAt(viewHolder.getAdapterPosition())))
                        .setNegativeButton("Cancel", (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition())).create().show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.wordOnItemClickListener(word -> {
            //navigate to home page just for trial
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        });

        fab.setOnClickListener(v -> {
            openAddNewWordDialog();
        });

        return rootView;
    }

    public void openAddNewWordDialog(){
        AddNewWordDialog addNewWordDialog = new AddNewWordDialog();
        addNewWordDialog.show(getChildFragmentManager(), "word dialog");
        addNewWordDialog.setOnSaveListener((word, translation) -> {
            wordViewModel.insertWord(new Word(setId, word, translation));
        });
    }

}
