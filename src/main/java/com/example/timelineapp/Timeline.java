package com.example.timelineapp;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "timeline_table")
public class Timeline {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer timelineID;

    @NonNull
    public String name;

    public String description;

    @NonNull
    public Boolean localOnly;

    @NonNull
    public Boolean showTimes;

    public Timeline () {

    }

    public Timeline(String name, String description, Boolean showTimes)  {
        this.localOnly = true;
        this.name = name;
        this.description = description;
        this.showTimes = showTimes;
    }
}
