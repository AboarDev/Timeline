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

    public Integer position;

    public Integer year;

    public Integer month;

    public Integer day;

    public Integer minute;

    public Integer second;


    public Entry (@NonNull Integer timelineID, @NonNull String title, String text, Integer position) {
        this.timelineID = timelineID;
        this.title = title;
        this.text = text;
        this.position = position;
    }
    @Ignore
    public Entry (Integer timelineID, String text, Integer year, Integer month, Integer day, Integer minute, Integer second) {
        this.timelineID = timelineID;
        this.text = text;
        this.year = year;
        this.month = month;
        this.day = day;
        this.minute = minute;
        this.second = second;
        this.position = null;
    }
}
