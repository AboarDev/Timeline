package com.example.timelineapp;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private Repository mRepository;
    private List<String> theStrs;
    //public int selected;
    public AppViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        //theStrs = new ArrayList<String>();
    }
    public void saveUri(Uri inp, Boolean isFile){
        String inpString = inp.toString();
        if (isFile){
            for (String str: theStrs) {
                if (str.contentEquals(inpString)){
                    return;
                }
            }
        }
        //theStrs.add(inpString);
        mRepository.addMediaBinding(1,inpString,"IMAGE");

    }

    public LiveData<List<Timeline>> getAllTimelines () {
        return mRepository.getAllTimelines();
    }

    public void addTimeline (String name,String description, Boolean showTimes) {
        mRepository.addTimeline(name,description,showTimes);
    }

    public void deleteTimeline (int id) {
        mRepository.deleteTimeline(id);
    }
}
