package com.example.flickrexample.db.dao;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.flickrexample.db.FlickrDatabase;
import com.example.flickrexample.db.entity.FlickrImageEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class FlickrImageDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FlickrDatabase mDatabase;

    private FlickrImageDao mFlickrImageDao;

    @Before
    public void initDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), FlickrDatabase.class)
                        .allowMainThreadQueries()
                        .build();

        mFlickrImageDao = mDatabase.flickrImageDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void searchFlickrImages_FlickrImagesInserted_ReturnsEmptyDataSource() {
        DataSource.Factory<Integer, FlickrImageEntity> dataSource = mFlickrImageDao.searchFlickrImages("Kittens");

        ((PositionalDataSource<FlickrImageEntity>) dataSource .create()).loadRange(
                new PositionalDataSource.LoadRangeParams(0, 10),
                new PositionalDataSource.LoadRangeCallback<FlickrImageEntity>() {
                    @Override
                    public void onResult(@NonNull List<FlickrImageEntity> data) {
                        assertEquals(0, data.size());
                    }
        });
    }

    @Test
    public void searchFlickrImages_WithFlickrImagesInserted_ReturnsEmptyDataSource() {
        List<FlickrImageEntity> flickrImageEntities = new ArrayList<>();
        flickrImageEntities.add(new FlickrImageEntity("ID", "Kittens Title", "server", "secret", "Kittens", 10, 1));
        mFlickrImageDao.insertAll(flickrImageEntities);

        DataSource.Factory<Integer, FlickrImageEntity> dataSource  = mFlickrImageDao.searchFlickrImages("Kittens");

        ((PositionalDataSource<FlickrImageEntity>) dataSource .create()).loadRange(
                new PositionalDataSource.LoadRangeParams(0, 10),
                new PositionalDataSource.LoadRangeCallback<FlickrImageEntity>() {
                    @Override
                    public void onResult(@NonNull List<FlickrImageEntity> data) {
                        assertEquals(1, data.size());
                        assertEquals(1, data.get(0).getId());
                    }
                });
    }

}
