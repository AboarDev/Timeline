package com.example.timelineapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimelineViewModel extends AndroidViewModel {
    static SimpleDateFormat formatTime = new SimpleDateFormat("dd/MM/yyyy - h:mm");
    private Repository mRepository;
    private Integer selected;
    public TimelineViewModel (Application application){
        super(application);
        mRepository = new Repository(application);
    }
    public void setTimeline(int i){
        selected = i;
    }
    public void addEntry (String title, String text, Integer position) {
        mRepository.addEntry(selected,title,text,position,formatTime.format(new Date()));
    }
    public void deleteEntry (int id) {
        mRepository.deleteEntry(id);
    }
    public LiveData<List<Entry>> getAllEntries () {
        return mRepository.getAllEntries(selected);
    }
    public LiveData<Timeline> getTimeline (int id) {
        return mRepository.getByIDLive(id);
    }
    public String getTitle () {
        return mRepository.getTimelineWithID(selected).name;
    }

    public void setURI (int id, String uri){
        mRepository.setURI(id,uri);
    }

    public void editEntry(int id, String title, String text){
        mRepository.editEntry(id,title,text);
    }
}
