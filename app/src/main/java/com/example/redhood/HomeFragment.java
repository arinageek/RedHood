package com.example.redhood;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private SQLiteDatabase db;
    private EditText etOrigWord;
    private EditText etTransWord;
    private Button addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        WordsDbHelper dbHelper = new WordsDbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        etOrigWord = view.findViewById(R.id.et_origWord);
        etTransWord = view.findViewById(R.id.et_transWord);
        addBtn = view.findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Write data
                ContentValues values = new ContentValues();
                values.put(WordsContract.Words.COLUMN_NAME_ORIGINAL, etOrigWord.getText().toString());
                values.put(WordsContract.Words.COLUMN_NAME_TRANSLATION, etTransWord.getText().toString());
                long newRowId = db.insert(WordsContract.Words.TABLE_NAME, null, values);
            }
        });

        return view;
    }
}
