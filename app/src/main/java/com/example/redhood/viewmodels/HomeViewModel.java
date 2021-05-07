package com.example.redhood.viewmodels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.redhood.database.SetRepository;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private SetRepository repository;
    private LiveData<List<Set>> allSets;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new SetRepository(application);
        allSets = repository.getAllSets();
    }

    //Word tasks
    public void insertWord(Word word){
        repository.insertWord(word);
    }

    public LiveData<List<Set>> getAllSets(){
        return allSets;
    }

}