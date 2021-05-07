package com.example.redhood.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Map;

@Entity(tableName = "set_table")
public class Set {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public Set(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){ this.name = name; }
}
