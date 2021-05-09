package com.example.redhood.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.redhood.database.dao.SetDao;
import com.example.redhood.database.dao.WordDao;
import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

@Database(entities = {Set.class, Word.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance;

    public abstract SetDao setDao();
    public abstract WordDao wordDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,"set_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDbAsyncTask(instance).execute();
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private SetDao setDao;

        private PopulateDbAsyncTask(AppDatabase db){
            setDao = db.setDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDao.insert(new Set("Example set 1"));
            return null;
        }
    }

}
