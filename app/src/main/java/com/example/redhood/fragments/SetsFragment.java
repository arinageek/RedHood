package com.example.redhood.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.redhood.adapters.SetAdapter;
import com.example.redhood.dialogs.AddNewSetDialog;
import com.example.redhood.dialogs.AddNewWordDialog;
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

        setViewModel = ViewModelProviders.of(this).get(SetViewModel.class);
        setViewModel.getAllSets().observe(getActivity(), sets -> {
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

        adapter.setOnItemClickListener(set -> {
            //navigate to a single set page
            WordsFragment fragment = new WordsFragment();
            Bundle arguments = new Bundle();
            arguments.putString("set_id" , String.valueOf(set.getId()));
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
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
}
