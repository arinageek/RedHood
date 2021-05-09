package com.example.redhood.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.redhood.database.dao.SetDao;
import com.example.redhood.database.dao.WordDao;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;
import com.example.redhood.translation.Translate;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataRepository {
    private SetDao setDao;
    private WordDao wordDao;
    private LiveData<List<Set>> allSets;

    public DataRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        setDao = database.setDao();
        wordDao = database.wordDao();
        allSets = setDao.getAllSets();
    }

    //Set functions
    public void insert(Set set){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> setDao.insert(set));
    }
    public void update(Set set){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> setDao.update(set));
    }
    public void delete(Set set){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> setDao.delete(set));
    }

    public void deleteAllWordsFrom(Set set){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> setDao.deleteAllWordsFrom(set.getId()));
    }

    public LiveData<List<Set>> getAllSets(){
        return allSets;
    }

    //Word functions
    public void insertWord(Word word){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> wordDao.insertWord(word));
    }
    public void updateWord(Word word){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> wordDao.updateWord(word));
    }
    public void deleteWord(Word word){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> wordDao.deleteWord(word));
    }

    //Relations functions
    public LiveData<List<SetWithWords>> getSetWithWords(int setId){ return wordDao.getSetWithWords(setId);}

    //Network functions
    public void getTranslation(final String original, final RepositoryCallback<String> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String translation="";
            try {
                Translate translateRequest = new Translate();
                String response = translateRequest.Post(original);
                JSONArray jsonResponse = new JSONArray(response);
                translation = jsonResponse.getJSONObject(0).getJSONArray("translations").getJSONObject(0).getString("text");
                callback.onComplete(translation);
            } catch (Exception e) {
                Log.d("Error", "Error in getTranslation()");
            }
        });
    }

}
