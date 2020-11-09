package com.example.timelineapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimelineDao {

    @Query("SELECT * from timeline_table")
    List<Timeline> getAll();

    @Query("SELECT * from timeline_table")
    LiveData<List<Timeline>> getAllLive();

    @Query("SELECT * from timeline_table where timelineID like (:id)")
    Timeline getByID(int id);

    @Query("SELECT * from timeline_table where name like (:name)")
    Timeline getByName(String name);

    @Query("SELECT * from timeline_table where timelineID like (:id)")
    LiveData<Timeline> getByIDLive(int id);

    @Insert
    void insert(Timeline timeline);

    @Update
    void updateTimeline (Timeline timeline);

    @Delete
    void deleteTimeline (Timeline timeline);
}
