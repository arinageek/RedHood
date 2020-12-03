package com.example.redhood;

import android.provider.BaseColumns;

public final class WordsContract {
    private WordsContract(){}

    public static class Words implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_ORIGINAL = "original";
        public static final String COLUMN_NAME_TRANSLATION = "translation";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
