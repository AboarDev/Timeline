package com.example.timelineapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "entry_table")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer entryID;
    @NonNull
    public Integer timelineID;
    @NonNull
    public String title;
    public String text;
    public String URI;
    public Integer position;
    public String dateTime;


    public Entry (@NonNull Integer timelineID, @NonNull String title, String text, Integer position) {
        this.timelineID = timelineID;
        this.title = title;
        this.text = text;
        this.position = position;
        this.URI = null;
        this.dateTime = null;
    }
    @Ignore
    public Entry (Integer timelineID, String text, Integer year, Integer month, Integer day, Integer minute, Integer second) {
        this.timelineID = timelineID;
        this.text = text;
        this.position = null;
    }
}
