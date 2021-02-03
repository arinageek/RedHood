package com.example.redhood.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.redhood.database.SetDao;
import com.example.redhood.database.SetDatabase;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;

import java.util.List;

public class SetRepository {
    private SetDao setDao;
    private LiveData<List<Set>> allSets;

    public SetRepository(Application application){
        SetDatabase database = SetDatabase.getInstance(application);
        setDao = database.setDao();
        allSets = setDao.getAllSets();
    }

    //Set functions
    public void insert(Set set){
        new InsertSetAsyncTask(setDao).execute(set);
    }
    public void update(Set set){
        new UpdateSetAsyncTask(setDao).execute(set);
    }
    public void delete(Set set){
        new DeleteSetAsyncTask(setDao).execute(set);
    }
    public void deleteAllWordsFrom(Set set){
        new DeleteAllWordsFromAsyncTask(setDao).execute(set);
    }
    public LiveData<List<Set>> getAllSets(){
        return allSets;
    }

    //Word functions
    public void insertWord(Word word){ new InsertWordAsyncTask(setDao).execute(word); }
    public void updateWord(Word word){
        new UpdateWordAsyncTask(setDao).execute(word);
    }
    public void deleteWord(Word word){
        new DeleteWordAsyncTask(setDao).execute(word);
    }

    //Relations functions
    public LiveData<List<SetWithWords>> getSetWithWords(int setId){ return setDao.getSetWithWords(setId);}

    //Set async tasks
    private static class InsertSetAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private InsertSetAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Set... sets) {
            setDao.insert(sets[0]);
            return null;
        }
    }

    private static class UpdateSetAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private UpdateSetAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Set... sets) {
            setDao.update(sets[0]);
            return null;
        }
    }

    private static class DeleteSetAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private DeleteSetAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Set... sets) {
            setDao.delete(sets[0]);
            return null;
        }
    }

    private static class DeleteAllWordsFromAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private DeleteAllWordsFromAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Set... sets) {
            setDao.deleteAllWordsFrom(sets[0].getId());
            return null;
        }
    }

    //Word async tasks
    private static class InsertWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private SetDao setDao;

        private InsertWordAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            setDao.insertWord(words[0]);
            return null;
        }
    }

    private static class UpdateWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private SetDao setDao;

        private UpdateWordAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            setDao.updateWord(words[0]);
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private SetDao setDao;

        private DeleteWordAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            setDao.deleteWord(words[0]);
            return null;
        }
    }

}
