package com.example.redhood;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.redhood.database.SetRepository;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

import java.util.List;

public class SetViewModel extends AndroidViewModel {

    private SetRepository repository;
    private LiveData<List<Set>> allSets;

    public SetViewModel(@NonNull Application application) {
        super(application);
        repository = new SetRepository(application);
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

    //Word tasks
    public void insertWord(Word word){
        repository.insertWord(word);
    }
    public void updateWord(Word word){
        repository.updateWord(word);
    }
    public void deleteWord(Word word){
        repository.deleteWord(word);
    }

    public void getSetWithWords(Set set){repository.getSetWithWords(set);}
}