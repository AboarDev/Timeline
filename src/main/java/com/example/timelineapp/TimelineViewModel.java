package com.example.timelineapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TimelineViewModel extends AndroidViewModel {
    private Repository mRepository;
    private Integer selected;
    public TimelineViewModel (Application application){
        super(application);
        mRepository = new Repository(application);
    }
    public void setTimeline(int i){
        selected = i;
    }
    public int getID(){
        return selected;
    }
    public void addEntry (String title, String text, Integer position) {
        mRepository.addEntry(selected,title,text,position);
    }
    public LiveData<List<Entry>> getAllEntries () {
        return mRepository.getAllEntries(selected);
    }
    public String getTitle () {
        return mRepository.getTimelineWithID(selected).name;
    }

    public void setURI (int id, String uri){
        mRepository.setURI(id,uri);
    }
}
