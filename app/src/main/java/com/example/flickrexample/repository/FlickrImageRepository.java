package com.example.flickrexample.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.flickrexample.AppExecutors;
import com.example.flickrexample.db.FlickrDatabase;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.network.FlickrWebService;
import com.example.flickrexample.network.WebService;

import java.util.List;

/**
 * This Repository communicates both with the Room database as well as the WebService to load images from the two sources.
 * It may be useful to introduce the concept of UseCases as an extra layer if this project were to grow.
 * The ViewModel uses LiveData to communicate with the Repository and receive updates when the data is loaded from one of the two data sources.
 *
 * The repository exposes a single LiveData object which propagates changes as a Resource which encapsulates the data, it's loading state, and error state.
 */
public class FlickrImageRepository implements FlickrImageRepositoryInterface {

    @Nullable
    private static FlickrImageRepository sInstance;

    private int mPageSize;
    private FlickrDatabase mDatabase;
    private AppExecutors mAppExecutors;
    private FlickrWebService mWebService;
    private MediatorLiveData<Resource<PagedList<FlickrImageEntity>>> mFlickrImageListResource;
    private LiveData<PagedList<FlickrImageEntity>> dbSource;

    public FlickrImageRepository(int pageSize, final FlickrDatabase database, final AppExecutors appExecutors, final FlickrWebService webService) {
        mPageSize = pageSize;
        mWebService = webService;
        mDatabase = database;
        mAppExecutors = appExecutors;

        mFlickrImageListResource = new MediatorLiveData<>();
    }

    public static FlickrImageRepository getInstance(int pageSize, final FlickrDatabase database, AppExecutors appExecutors, FlickrWebService webService) {
        if (sInstance == null) {
            synchronized (FlickrImageRepository.class) {
                if (sInstance == null) {
                    sInstance = new FlickrImageRepository(pageSize, database, appExecutors, webService);
                }
            }
        }
        return sInstance;
    }

    public LiveData<Resource<PagedList<FlickrImageEntity>>> getFlickrImages() {
        return mFlickrImageListResource;
    }

    /**
     * Search for images using the string provided.
     * Updates in the loaded images can be observed with the LiveData object returned by {@link #getFlickrImages()}.
     */
    public void searchImages(String searchString) {
        if (dbSource != null){
            mFlickrImageListResource.removeSource(dbSource);
        }

        dbSource = searchFlickrImagesInDb(searchString, new PagedListBoundaryCallback(searchString, this));

        mFlickrImageListResource.addSource(dbSource, flickrImageEntities -> mFlickrImageListResource.setValue(Resource.success(flickrImageEntities)));
    }

    /**
     * Trigger a fetch which loads new images from the WebService and inserts them into the Room DB.
     */
    public void fetchNewFlickrImages(final String searchString, int pageNumber) {
        mFlickrImageListResource.removeSource(dbSource);
        mFlickrImageListResource.addSource(dbSource, newData -> mFlickrImageListResource.setValue(Resource.loading(newData)));

        mWebService.getFlickrImages(searchString, pageNumber, new WebService.WebServiceCallback<List<FlickrImageEntity>>() {
            @Override
            public void onResponse(final List<FlickrImageEntity> flickrImageEntities) {
                if (flickrImageEntities == null) {
                    return;
                }

                mFlickrImageListResource.removeSource(dbSource);

                for (FlickrImageEntity entity : flickrImageEntities) {
                    entity.setSearchTerm(searchString);
                }

                mAppExecutors.diskIO().execute(() -> {
                    mDatabase.flickrImageDao().insertAll(flickrImageEntities);

                    mAppExecutors.mainThread().execute(() -> {
                        mFlickrImageListResource.addSource(dbSource, newData -> mFlickrImageListResource.setValue(Resource.success(newData)));
                    });
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                mFlickrImageListResource.removeSource(dbSource);
                mFlickrImageListResource.addSource(dbSource, newData -> mFlickrImageListResource.setValue(Resource.error(errorMessage, newData)));
            }
        });
    }

    /**
     * Load the images from the Room DB using the PagedList library so that we only get a page of data at a time.
     */
    private LiveData<PagedList<FlickrImageEntity>> searchFlickrImagesInDb(String searchString, final PagedList.BoundaryCallback<FlickrImageEntity> boundaryCallback) {
        PagedList.Config myPagingConfig = new PagedList.Config.Builder()
                .setPageSize(mPageSize)
                .setPrefetchDistance(150)
                .setEnablePlaceholders(true)
                .build();

        DataSource.Factory<Integer, FlickrImageEntity> dataSource = mDatabase.flickrImageDao().searchFlickrImages(searchString);

        return new LivePagedListBuilder<>(dataSource, myPagingConfig).setBoundaryCallback(boundaryCallback).build();
    }

}
