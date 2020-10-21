package com.example.timelineapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_table")
public class MediaBinding {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer bindingID;

    @NonNull
    public Integer EntryID;

    @NonNull
    public String fileLocation;

    @NonNull
    public String type;

    public MediaBinding () {

    }

    public MediaBinding (Integer EntryID, String fileLocation, String type) {
        this.EntryID = EntryID;
        this.fileLocation = fileLocation;
        this.type = type;
    }
}
