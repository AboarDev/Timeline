package com.example.timelineapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * from entry_table where timelineID LIKE (:inp)")
    LiveData<List<Entry>> getAllWithTimelineID(int inp);

    @Query("SELECT * from entry_table where timelineID LIKE (:id) and position LIKE (:pos)")
    Entry getWithTimelineIDAndPos(int id, int pos);

    @Insert
    void insert(Entry entry);
}
