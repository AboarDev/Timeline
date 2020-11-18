package com.example.timelineapp;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimelineViewModel extends AndroidViewModel {
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat formatTime = new SimpleDateFormat("dd/MM/yyyy - h:mm");
    private Repository mRepository;
    public TimelineViewModel (Application application){
        super(application);
        mRepository = new Repository(application);
    }
    public void addEntry (int timelineID, String title, String text, Integer position) {
        mRepository.addEntry(timelineID,title,text,position,formatTime.format(new Date()));
    }
    public void deleteEntry (int id) {
        mRepository.deleteEntry(id);
    }
    public LiveData<List<Entry>> getAllEntries (int timelineID) {
        return mRepository.getAllEntries(timelineID);
    }
    public LiveData<Timeline> getTimeline (int id) {
        return mRepository.getByIDLive(id);
    }
    public void setURI (int id, String uri){
        mRepository.setURI(id,uri);
    }
    public void editEntry(int id, String title, String text){
        mRepository.editEntry(id,title,text);
    }
    public void removeImage(int id){
        mRepository.removeImage(id);
    }
}
