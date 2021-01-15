package com.example.redhood.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redhood.R;
import com.example.redhood.SetAdapter;
import com.example.redhood.dialogs.AddNewSetDialog;
import com.example.redhood.viewmodels.SetViewModel;
import com.example.redhood.database.entities.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SetsFragment extends Fragment {

    private RecyclerView recyclerView;
    private static SetViewModel setViewModel;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sets, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_sets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab = rootView.findViewById(R.id.fab);

        final SetAdapter adapter = new SetAdapter();
        recyclerView.setAdapter(adapter);

        setViewModel = ViewModelProviders.of(this).get(SetViewModel.class);
        setViewModel.getAllSets().observe(getActivity(), new Observer<List<Set>>() {
            @Override
            public void onChanged(List<Set> sets) {
                //update RecyclerView
                adapter.submitList(sets);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                setViewModel.delete(adapter.getSetAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Set deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Set set) {
                //navigate to a single set page
                Toast.makeText(getActivity(), "Navigated to a single set page", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return rootView;
    }

    public void openDialog(){
        AddNewSetDialog addNewSetDialog = new AddNewSetDialog();
        addNewSetDialog.show(getActivity().getSupportFragmentManager(), "set dialog");
    }

    public static void applyTexts(String title) {
        setViewModel.insert(new Set(title));
    }
}
