package com.example.flickrexample.db.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.flickrexample.db.entity.FlickrImageEntity;

import java.util.List;

@Dao
public interface FlickrImageDao {

    @Query("SELECT * FROM flickrImages WHERE searchTerm = :searchTerm ORDER BY id ASC")
    DataSource.Factory<Integer, FlickrImageEntity> searchFlickrImages(String searchTerm);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FlickrImageEntity> flickrImages);

}
