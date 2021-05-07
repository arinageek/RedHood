package com.example.redhood.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.redhood.R;

public class EditSetDialog extends DialogFragment {
    private EditText edit_title;
    private OnSaveListener onSaveListener;

    public interface OnSaveListener{
        void onSaveEditedSet(String title);
    }

    public void setOnSaveListener(OnSaveListener listener){ onSaveListener = listener; }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_edit_set_dialog, null);

        edit_title = view.findViewById(R.id.edit_title);
        edit_title.setText(getArguments().getString("set_title"));

        builder.setView(view)
                .setTitle("Edit the set")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = edit_title.getText().toString();
                    if(onSaveListener!=null) onSaveListener.onSaveEditedSet(title);
                });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveListener = null;
    }
}
