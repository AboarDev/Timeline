package com.example.timelineapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TimelineViewModel extends AndroidViewModel {
    private Repository mRepository;
    private int selected;
    public TimelineViewModel (Application application){
        super(application);
        mRepository = new Repository(application);
    }
    public void setTimeline(int i){
        selected = i;
    }
    public LiveData<List<Entry>> getAllEntries () {
        return mRepository.getAllEntries(selected);
    }
    public String getTitle () {
        return mRepository.getTimelineWithID(selected).name;
    }
}
