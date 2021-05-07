package com.example.redhood.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.redhood.R;
import com.example.redhood.database.SetRepository;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;
import com.example.redhood.fragments.HomeFragment;
import com.example.redhood.fragments.SetsFragment;

import java.util.ArrayList;
import java.util.List;

public class ChooseSetDialog extends DialogFragment {

    private int choice=0;
    private OnSelectedListener onSelectedListener;
    private static CharSequence[] setsTitles;

    public interface OnSelectedListener { void onSetSelected(int position); }
    public void setOnSelectedListener(ChooseSetDialog.OnSelectedListener listener){ onSelectedListener = listener; }

    public ChooseSetDialog(CharSequence[] sets){
        setsTitles = sets;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add to set")
                .setSingleChoiceItems(setsTitles, 0, (dialog, which) -> choice = which)
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Save", (dialog, which) -> {
                    if(onSelectedListener != null) onSelectedListener.onSetSelected(choice);
                });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelectedListener = null;
    }
}
