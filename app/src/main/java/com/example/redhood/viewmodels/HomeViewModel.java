package com.example.redhood.viewmodels;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.redhood.database.RepositoryCallback;
import com.example.redhood.database.DataRepository;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private DataRepository repository;
    private LiveData<List<Set>> allSets;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        allSets = repository.getAllSets();
    }

    public void insertWord(Word word){
        repository.insertWord(word);
    }

    public LiveData<List<Set>> getAllSets(){
        return allSets;
    }

    public void getTranslation(String original, RepositoryCallback<String> callback, Context context){ repository.getTranslation(original,callback, context );}

}