package com.example.redhood.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redhood.MainActivity;
import com.example.redhood.R;
import com.example.redhood.adapters.WordAdapter;
import com.example.redhood.database.entities.Word;
import com.example.redhood.dialogs.AddNewWordDialog;
import com.example.redhood.dialogs.EditSetDialog;
import com.example.redhood.dialogs.EditWordDialog;
import com.example.redhood.viewmodels.SetViewModel;
import com.example.redhood.viewmodels.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class WordsFragment extends Fragment {

    private FragmentManager fragmentManager;
    private static RecyclerView recyclerView;
    private static WordViewModel wordViewModel;
    private static WordAdapter adapter;
    private FloatingActionButton fab;
    private Button playBtn;
    private static int setId;
    private TextView set_title;
    private int set_size=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_words, container, false);

        fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();

        fab = rootView.findViewById(R.id.fab);
        playBtn = rootView.findViewById(R.id.playBtn);
        set_title = rootView.findViewById(R.id.set_title);
        recyclerView = rootView.findViewById(R.id.recyclerViewWords);
        adapter = new WordAdapter();
        recyclerView.setAdapter(adapter);

        setId = getArguments().getInt("set_id", 0);
        set_title.setText(getArguments().getString("set_title", "Set"));

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        wordViewModel.getSetWithWords(setId).observe(getViewLifecycleOwner(), setWithWords -> {
            if (setWithWords.isEmpty()) return;
            //update RecyclerView
            set_size = setWithWords.get(0).words.size();
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

        adapter.wordOnItemClickListener(new WordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Word word) {
                SingleWordFragment fragment = new SingleWordFragment();
                Bundle arguments = new Bundle();
                arguments.putString("word", String.valueOf(word.getOriginal()));
                fragment.setArguments(arguments);
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        fragment).addToBackStack(null).commit();
            }

            @Override
            public void onItemLongClick(Word word, int position) {
                openEditWordDialog(word, position);
            }
        });

        fab.setOnClickListener(v -> {
            openAddNewWordDialog();
        });

        playBtn.setOnClickListener(v -> {
            if (set_size >= 4) {
                GameFragment fragment = new GameFragment();
                Bundle arguments = new Bundle();
                arguments.putString("set_id", String.valueOf(setId));
                fragment.setArguments(arguments);
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        fragment, "game").addToBackStack(null).commit();
            } else {
                Toast.makeText(getActivity(), "There must be at least 4 words in a set to play!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void openAddNewWordDialog() {
        AddNewWordDialog addNewWordDialog = new AddNewWordDialog();
        addNewWordDialog.show(getChildFragmentManager(), "word dialog");
        addNewWordDialog.setOnSaveListener((word, translation) -> {
            wordViewModel.insertWord(new Word(setId, word, translation));
        });
    }

    public void openEditWordDialog(Word oldWord, int position) {
        EditWordDialog dialog = new EditWordDialog();
        Bundle arguments = new Bundle();
        arguments.putString("word_original", String.valueOf(oldWord.getOriginal()));
        arguments.putString("word_translation", String.valueOf(oldWord.getTranslation()));
        dialog.setArguments(arguments);
        dialog.show(getChildFragmentManager(), "edit_word_dialog");
        dialog.setOnSaveListener((original, translation) -> {
            Word editedWord = oldWord;
            editedWord.setOriginal(original);
            editedWord.setTranslation(translation);
            wordViewModel.updateWord(editedWord);
            adapter.notifyItemChanged(position);
        });
    }
}
