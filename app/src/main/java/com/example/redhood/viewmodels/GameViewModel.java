package com.example.redhood.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.redhood.database.DataRepository;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;

import java.util.List;

public class GameViewModel extends AndroidViewModel {

    private DataRepository repository;

    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
    }

    public LiveData<List<SetWithWords>> getSetWithWords(int setId){return repository.getSetWithWords(setId);}

}