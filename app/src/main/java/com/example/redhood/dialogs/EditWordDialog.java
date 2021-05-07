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

public class EditWordDialog extends DialogFragment {
    private EditText et_orig;
    private EditText et_trans;
    private OnSaveListener onSaveListener;

    public interface OnSaveListener{
        void onSaveEditedWord(String word, String translation);
    }
    public void setOnSaveListener(OnSaveListener listener) {
        onSaveListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_word_dialog, null);

        et_orig = view.findViewById(R.id.et_origWord);
        et_trans = view.findViewById(R.id.et_transWord);

        et_orig.setText(getArguments().getString("word_original"));
        et_trans.setText(getArguments().getString("word_translation"));

        builder.setView(view)
                .setTitle("Edit the word")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Save", (dialog, which) -> {
                    String word = et_orig.getText().toString();
                    String translation = et_trans.getText().toString();
                    if(onSaveListener!=null) onSaveListener.onSaveEditedWord(word, translation);
                });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveListener = null;
    }
}
