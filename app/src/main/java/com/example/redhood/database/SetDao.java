package com.example.redhood.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;

import java.util.List;

@Dao
public interface SetDao {

    @Insert
    void insert(Set set);

    @Update
    void update(Set set);

    @Delete
    void delete(Set set);

    @Insert
    void insertWord(Word word);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteWord(Word word);

    @Transaction
    @Query("SELECT * FROM word_table WHERE setId = :setId")
    public List<SetWithWords> getSetWithWords(int setId);

    @Query("SELECT * FROM set_table")
    LiveData<List<Set>> getAllSets();

}
