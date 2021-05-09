package com.example.redhood.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.redhood.database.DataRepository;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private DataRepository repository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public LiveData<List<SetWithWords>> getSetWithWords(int setId){return repository.getSetWithWords(setId);}
    public void deleteWord(Word word){ repository.deleteWord(word);}
    public void insertWord(Word word){ repository.insertWord(word);}
    public void updateWord(Word word){ repository.updateWord(word);}
}