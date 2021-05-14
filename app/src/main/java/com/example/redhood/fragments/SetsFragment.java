package com.example.redhood.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.redhood.adapters.SetAdapter;
import com.example.redhood.dialogs.AddNewSetDialog;
import com.example.redhood.dialogs.AddNewWordDialog;
import com.example.redhood.dialogs.EditSetDialog;
import com.example.redhood.viewmodels.SetViewModel;
import com.example.redhood.database.entities.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SetsFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static SetViewModel setViewModel;
    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private static SetAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sets, container, false);

        fab = rootView.findViewById(R.id.fab);

        //TRY CHANGING THIS TO ParentFragmentManager
        fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();

        recyclerView = rootView.findViewById(R.id.recycler_view_sets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SetAdapter();
        recyclerView.setAdapter(adapter);

        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);
        setViewModel.getAllSets().observe(getViewLifecycleOwner(), sets -> {
            //update RecyclerView
            adapter.submitList(sets);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Do you want to delete this set?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            setViewModel.deleteAllWordsFrom(adapter.getSetAt(viewHolder.getAdapterPosition()));
                            setViewModel.delete(adapter.getSetAt(viewHolder.getAdapterPosition()));
                        })
                        .setNegativeButton("Cancel",
                                (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition())).create().show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Set set) {
                //navigate to a single set page
                WordsFragment fragment = new WordsFragment();
                Bundle arguments = new Bundle();
                arguments.putString("set_id" , String.valueOf(set.getId()));
                arguments.putString("set_title" , String.valueOf(set.getName()));
                fragment.setArguments(arguments);
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        fragment, "words").addToBackStack(null).commit();
            }

            @Override
            public void onItemLongClick(Set set, int position) {
                openEditSetDialog(set, position);
            }
        });

        fab.setOnClickListener(v -> openAddNewSetDialog());

        return rootView;
    }

    public void openAddNewSetDialog(){
        AddNewSetDialog addNewSetDialog = new AddNewSetDialog();
        addNewSetDialog.show(getChildFragmentManager(), "set dialog");
        addNewSetDialog.setOnSaveListener(title -> {
            setViewModel.insert(new Set(title));
        });
    }

    public void openEditSetDialog(Set set, int position){
        EditSetDialog dialog = new EditSetDialog();
        Bundle arguments = new Bundle();
        arguments.putString("set_title" , String.valueOf(set.getName()));
        dialog.setArguments(arguments);
        dialog.show(getChildFragmentManager(), "edit_set_dialog");
        dialog.setOnSaveListener(title -> {
            Set editedSet = set;
            editedSet.setName(title);
            setViewModel.update(editedSet);
            adapter.notifyItemChanged(position);
        });
    }
}
