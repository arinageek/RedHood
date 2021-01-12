package com.example.redhood.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Set.class, Word.class}, version = 1)
public abstract class SetDatabase extends RoomDatabase {

    public static SetDatabase instance;

    public abstract SetDao setDao();

    public static synchronized SetDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SetDatabase.class,"set_database")
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

        private PopulateDbAsyncTask(SetDatabase db){
            setDao = db.setDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDao.insert(new Set("Example set 1"));
            setDao.insert(new Set("Your second set"));
            return null;
        }
    }

}
