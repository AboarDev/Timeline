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

    Repository(Application application) {
        ModelDatabase db = ModelDatabase.getDatabase(application);
        mTimeLineDao = db.timelineDao();
        mEntryDao = db.entryDao();
        mMediaBindingDao = db.mediaBindingDao();
        mAllTimelines = mTimeLineDao.getAllLive();
    }

    public void addTimeline(String name, String description, Boolean showTimes) {
        Timeline theTimeline = new Timeline(name, description, showTimes);
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mTimeLineDao.insert(theTimeline);
        });
    }

    public void addEntry(Integer timelineID, String title, String text, Integer position, String dateTime) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mEntryDao.insert(new Entry(timelineID, title, text, position, dateTime));
        });
    }

    public void addMediaBinding(int id, String str, String type) {
        MediaBinding binding = new MediaBinding(id, str, type);
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mMediaBindingDao.insert(binding);
        });
    }

    public LiveData<List<Timeline>> getAllTimelines() {
        return mAllTimelines;
    }

    public Timeline getTimelineWithID(int id) {
        return mTimeLineDao.getByID(id);
    }

    public LiveData<List<Entry>> getAllEntries(int id) {
        return mEntryDao.getAllWithTimelineID(id);
    }

    public void deleteTimeline(int id) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            for (Entry e : mEntryDao.getWithTimelineID(id)) {
                mEntryDao.delete(e);
            }
            mTimeLineDao.deleteTimeline(mTimeLineDao.getByID(id));
        });
    }

    public void deleteEntry(int id) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            mEntryDao.delete(mEntryDao.getWithTimelineIDAndPos(id));
        });
    }

    public void setURI(int id, String uri) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            Entry entry = mEntryDao.getWithTimelineIDAndPos(id);
            entry.URI = uri;
            mEntryDao.update(entry);
        });
    }

    public void editEntry(int id, String title, String text) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            Entry entry = mEntryDao.getWithTimelineIDAndPos(id);
            if (title != null) {
                entry.title = title;
            }
            if (text != null) {
                entry.text = text;
            }
            mEntryDao.update(entry);
        });
    }

    public void removeImage(int id) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            Entry entry = mEntryDao.getWithTimelineIDAndPos(id);
            entry.URI = null;
            mEntryDao.update(entry);
        });
    }

    public void editTimeline(int id, String name, String description, Boolean showTimes) {
        ModelDatabase.databaseWriteExecutor.execute(() -> {
            Timeline timeline = mTimeLineDao.getByID(id);
            if (name != null) {
                timeline.name = name;
            }
            if (description != null) {
                timeline.description = description;
            }
            mTimeLineDao.updateTimeline(timeline);
        });
    }

    public LiveData<Timeline> getByIDLive(int id) {
        return mTimeLineDao.getByIDLive(id);
    }
}
