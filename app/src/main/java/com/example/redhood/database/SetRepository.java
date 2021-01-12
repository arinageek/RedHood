package com.example.redhood.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.redhood.database.SetDao;
import com.example.redhood.database.SetDatabase;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

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

    public void getSetWithWords(Set set){ new GetSetWithWordsAsyncTask(setDao).execute(set);}

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

    //Get words from a specific set
    private static class GetSetWithWordsAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private GetSetWithWordsAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }
        @Override
        protected Void doInBackground(Set... sets) {
            setDao.getSetWithWords(sets[0].getId());
            return null;
        }
    }

}
