package com.example.redhood;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetsFragment extends Fragment {

    private SQLiteDatabase db;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLinearLayoutManager;
    protected CustomAdapter mAdapter;
    protected ArrayList<Dictionary> dataSet = new ArrayList<Dictionary>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sets, container, false);

        WordsDbHelper dbHelper = new WordsDbHelper(getContext());
        db = dbHelper.getReadableDatabase();
        //Read data from database
        String[] projection = {
                BaseColumns._ID, WordsContract.Words.COLUMN_NAME_ORIGINAL,
                WordsContract.Words.COLUMN_NAME_TRANSLATION
        };
        Cursor cursor = db.query(
                WordsContract.Words.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                WordsContract.Words.COLUMN_TIMESTAMP+" DESC"
        );
        while(cursor.moveToNext()){
            String original = cursor.getString(cursor.getColumnIndex(WordsContract.Words.COLUMN_NAME_ORIGINAL));
            String translation = cursor.getString(cursor.getColumnIndex(WordsContract.Words.COLUMN_NAME_TRANSLATION));
            Dictionary obj = new Dictionary(original, translation);
            dataSet.add(obj);
        }
        cursor.close();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSets);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CustomAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
