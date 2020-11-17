package com.example.timelineapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private Repository mRepository;
    public AppViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    public LiveData<List<Timeline>> getAllTimelines () {
        return mRepository.getAllTimelines();
    }

    public void addTimeline (String name,String description, Boolean showTimes) {
        mRepository.addTimeline(name,description,showTimes);
    }

    public void editTimeline (int id, String name,String description, Boolean showTimes) {
        mRepository.editTimeline(id, name,description,showTimes);
    }

    public void deleteTimeline (int id) {
        mRepository.deleteTimeline(id);
    }
}
