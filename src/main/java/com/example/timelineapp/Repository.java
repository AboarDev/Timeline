package com.example.timelineapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {
    private TimelineDao mTimeLineDao;
    private MediaBindingDao mMediaBindingDao;
    private EntryDao mEntryDao;
    private LiveData<List<Timeline>> mAllTimelines;
    private LiveData<List<Entry>> mAllEntries;

    Repository (Application application) {
        ModelDatabase db = ModelDatabase.getDatabase(application);
        mTimeLineDao = db.timelineDao();
        mEntryDao = db.entryDao();
        mMediaBindingDao = db.mediaBindingDao();
        mAllTimelines = mTimeLineDao.getAllLive();
    }
    public void addTimeline(String name,String description, Boolean showTimes){
        Timeline theTimeline = new Timeline(name,description, showTimes);
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mTimeLineDao.insert(theTimeline);
        });
    }
    public void addEntry(Integer timelineID, String title, String text, Integer position){
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mEntryDao.insert(new Entry(timelineID,title,text,position));
        });
    }
    public void addMediaBinding(int id,String str, String type){
        MediaBinding binding = new MediaBinding(id,str, type);
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mMediaBindingDao.insert(binding);
        });
    }
    public LiveData<List<Timeline>> getAllTimelines () {
        return mAllTimelines;
    }

    public Timeline getTimelineWithID (int id){
        return mTimeLineDao.getByID(id);
    }

    public LiveData<List<Entry>> getAllEntries (int id) {
        return mEntryDao.getAllWithTimelineID(id);
    }

    public void deleteTimeline (int id) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mTimeLineDao.deleteTimeline(mTimeLineDao.getByID(id));
        });
    }
}
