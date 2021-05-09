package com.example.redhood.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.redhood.database.DataRepository;
import com.example.redhood.database.entities.Set;

import java.util.List;

public class SetViewModel extends AndroidViewModel {

    private DataRepository repository;
    private LiveData<List<Set>> allSets;

    public SetViewModel(@NonNull Application application) {
        super(application);
        repository = new DataRepository(application);
        allSets = repository.getAllSets();
    }

    //Set tasks
    public void insert(Set set){
        repository.insert(set);
    }
    public void update(Set set){
        repository.update(set);
    }
    public void delete(Set set){
        repository.delete(set);
    }
    public LiveData<List<Set>> getAllSets() {
        return allSets;
    }
    public void deleteAllWordsFrom(Set set){ repository.deleteAllWordsFrom(set);}

}