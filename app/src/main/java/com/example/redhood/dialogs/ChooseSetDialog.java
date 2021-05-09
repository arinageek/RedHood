package com.example.redhood.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
