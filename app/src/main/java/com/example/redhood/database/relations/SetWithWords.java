package com.example.redhood.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

import java.util.List;

public class SetWithWords {
    @Embedded public Set set;
    @Relation(
            parentColumn = "id",
            entityColumn = "setId"
    )
    public List<Word> words;
}
