package com.example.timelineapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaBindingDao {

    @Query("SELECT * from media_table")
    List<MediaBinding> getAll();

    @Query("SELECT * from media_table where EntryID LIKE (:inp)")
    List<MediaBinding> getAllWithEntryID(int inp);

    @Insert
    void insert (MediaBinding binding);
}
