package com.example.flickrexample.db;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.flickrexample.db.dao.FlickrImageDao;
import com.example.flickrexample.db.entity.FlickrImageEntity;

@Database(entities = {FlickrImageEntity.class}, version = 1)
public abstract class FlickrDatabase extends RoomDatabase {

    @Nullable
    private static FlickrDatabase sInstance;
    private static final String DATABASE_NAME = "flickr-images-db";

    public abstract FlickrImageDao flickrImageDao();

    public static FlickrDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (FlickrDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, FlickrDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}
