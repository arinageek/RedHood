package com.example.redhood;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.redhood.WordsContract.*;

import androidx.annotation.Nullable;

public class WordsDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WordsReader.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ Words.TABLE_NAME+" ("+
                    Words._ID + " INTEGER PRIMARY KEY," +
                    Words.COLUMN_TIMESTAMP+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
                    Words.COLUMN_NAME_ORIGINAL+ " TEXT,"+
                    Words.COLUMN_NAME_TRANSLATION+" TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Words.TABLE_NAME;

    public WordsDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
