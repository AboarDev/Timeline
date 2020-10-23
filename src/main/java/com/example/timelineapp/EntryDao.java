package com.example.timelineapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * from entry_table where timelineID LIKE (:inp)")
    LiveData<List<Entry>> getAllWithTimelineID(int inp);

    @Query("SELECT * from entry_table where entryID LIKE (:id)")
    Entry getWithTimelineIDAndPos(int id);

    @Query("SELECT * from entry_table where timelineID LIKE (:timelineID)")
    List<Entry> getWithTimelineID(int timelineID);

    @Insert
    void insert(Entry entry);

    @Update
    void update (Entry entry);

    @Delete
    void delete(Entry entry);
}
