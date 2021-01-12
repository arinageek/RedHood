package com.example.redhood.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int setId;

    private String original;

    private String translation;

    public Word(int setId, String original, String translation) {
        this.setId = setId;
        this.original = original;
        this.translation = translation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getSetId() {
        return setId;
    }

    public String getOriginal() {
        return original;
    }

    public String getTranslation() {
        return translation;
    }
}
