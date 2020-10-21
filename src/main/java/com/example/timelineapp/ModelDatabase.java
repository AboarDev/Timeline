package com.example.timelineapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Timeline.class, Entry.class, MediaBinding.class}, version = 1)
public abstract class ModelDatabase extends RoomDatabase {
    public abstract TimelineDao timelineDao();
    public abstract EntryDao entryDao();
    public abstract MediaBindingDao mediaBindingDao();


    private static volatile ModelDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static ModelDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (ModelDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ModelDatabase.class,"timeline_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
