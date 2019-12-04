package com.example.flickrexample.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.flickrexample.AppExecutors;
import com.example.flickrexample.db.FlickrDatabase;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.network.FlickrWebService;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class FlickrImageRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static FlickrDatabase mDatabase;

    private AppExecutors mAppExecutors;

    private FlickrWebService mWebservice;

    private Observer<Resource<PagedList<FlickrImageEntity>>> mObserver = pagedListResource -> { };

    @Before
    public void setup() {
        List<FlickrImageEntity> flickrImageEntities = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            flickrImageEntities.add(new FlickrImageEntity("ID", "Kittens Title", "server", "secret", "kittens", 10, 1));
        }

        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), FlickrDatabase.class)
                .allowMainThreadQueries()
                .build();
        mDatabase.flickrImageDao().insertAll(flickrImageEntities);

        mWebservice = new MockSuccessfulWebService(flickrImageEntities);

        mAppExecutors = MockAppExecutors.build();
    }

    @After
    public void clearDB() {
        mDatabase.clearAllTables();
    }

    @AfterClass
    public static void closeDB() {
        mDatabase.close();
    }

    @Test
    public void getInstance_ShouldReturnTheInstance() {
        FlickrImageRepository repository = FlickrImageRepository.getInstance(1, mDatabase, mAppExecutors, mWebservice);

        assertNotNull(repository);
    }

    @Test
    public void getFlickrImages_ReturnsTheLiveData() {
        FlickrImageRepository repository = new FlickrImageRepository(1, mDatabase, mAppExecutors, mWebservice);

        LiveData<Resource<PagedList<FlickrImageEntity>>> entities = repository.getFlickrImages();

        assertNotNull(entities);
    }

    @Test
    public void searchImages_WithNoMatchingEntities_ShouldCallOnChangeWithResourcesFromTheDB() {
        FlickrImageRepository repository = new FlickrImageRepository(1, mDatabase, mAppExecutors, mWebservice);
        LiveData<Resource<PagedList<FlickrImageEntity>>> entities = repository.getFlickrImages();
        entities.observeForever(mObserver);

        repository.searchImages("kittens");

        Resource<PagedList<FlickrImageEntity>> resource = entities.getValue();

        assertNotNull(resource);
        assertEquals(LoadingStatus.SUCCESS, resource.getLoadingStatus());
    }

    @Test
    public void fetchNewFlickrImages_WhenTheFetchSucceeds_ShouldUpdateWithSuccessResource() {
        FlickrImageRepository repository = new FlickrImageRepository(1, mDatabase, mAppExecutors, mWebservice);
        LiveData<Resource<PagedList<FlickrImageEntity>>> entities = repository.getFlickrImages();
        entities.observeForever(mObserver);
        repository.searchImages("kittens");

        repository.fetchNewFlickrImages("kittens", 2);

        Resource<PagedList<FlickrImageEntity>> resource = entities.getValue();
        assertNotNull(resource);
        assertEquals(LoadingStatus.LOADING, resource.getLoadingStatus());
    }

    @Test
    public void fetchNewFlickrImages_WhenTheFetchFails_ShouldUpdateWithFailResource() {
        FlickrImageRepository repository = new FlickrImageRepository(1, mDatabase, mAppExecutors, new MockFailWebService());
        LiveData<Resource<PagedList<FlickrImageEntity>>> entities = repository.getFlickrImages();
        entities.observeForever(mObserver);
        repository.searchImages("kittens");

        repository.fetchNewFlickrImages("kittens", 2);

        Resource<PagedList<FlickrImageEntity>> resource = entities.getValue();

        assertNotNull(resource);
        assertEquals(LoadingStatus.ERROR, resource.getLoadingStatus());
        assertEquals("Error!", resource.getErrorMessage());
    }


    class MockSuccessfulWebService extends FlickrWebService {

        private List<FlickrImageEntity> mFlickrImageEntities;

        MockSuccessfulWebService(List<FlickrImageEntity> flickrImageEntities) {
            super(null);

            mFlickrImageEntities = flickrImageEntities;
        }

        @Override
        public void getFlickrImages(String searchString, int pageNumber, final WebServiceCallback<List<FlickrImageEntity>> callback) {
            callback.onResponse(mFlickrImageEntities);
        }
    }

    class MockFailWebService extends FlickrWebService {

        MockFailWebService() {
            super(null);
        }

        @Override
        public void getFlickrImages(String searchString, int pageNumber, final WebServiceCallback<List<FlickrImageEntity>> callback) {
            callback.onFailure("Error!");
        }
    }

    static class MockAppExecutors extends AppExecutors {

        MockAppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
            super(diskIO, networkIO, mainThread);
        }

        static MockAppExecutors build() {
            Executor e = new MainThreadExecutor();
            return new MockAppExecutors(e, e, e);
        }


        private static class MainThreadExecutor implements Executor {
            private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(@NonNull Runnable command) {
                mainThreadHandler.post(command);
            }
        }
    }

}
