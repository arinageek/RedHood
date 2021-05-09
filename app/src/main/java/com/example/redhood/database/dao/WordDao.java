package com.example.redhood.database.dao;

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
public interface WordDao {

    @Insert
    void insertWord(Word word);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteWord(Word word);

    @Transaction
    @Query("SELECT * FROM set_table WHERE id = :setId")
    LiveData<List<SetWithWords>> getSetWithWords(int setId);

}
