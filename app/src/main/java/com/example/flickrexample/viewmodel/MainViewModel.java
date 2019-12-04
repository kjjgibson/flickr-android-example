package com.example.flickrexample.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.example.flickrexample.ServiceLocator;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.repository.FlickrImageRepositoryInterface;

import static com.example.flickrexample.repository.LoadingStatus.LOADING;

public class MainViewModel extends AndroidViewModel {

    private static final int PAGE_SIZE = 100;

    private final FlickrImageRepositoryInterface mRepository;
    private final MediatorLiveData<PagedList<FlickrImageEntity>> mFlickrImageListLiveData;
    private final MutableLiveData<Boolean> mIsLoadingLiveData;
    private final MutableLiveData<Boolean> mFetchErrorLiveData;
    private final MutableLiveData<String> mSearchStringLiveData;

    public MainViewModel(Application application) {
        super(application);

        mRepository = ServiceLocator.getInstance(application).getRepository(PAGE_SIZE);

        mFetchErrorLiveData = new MutableLiveData<>();
        mSearchStringLiveData = new MutableLiveData<>();
        mIsLoadingLiveData = new MutableLiveData<>();
        mIsLoadingLiveData.setValue(false);

        // The Repository exposes a LiveData that delivers changes as a Resource.
        // I want to map the single Resource into three different LiveData objects:
        // the list of images, the network status, and the error status so that the UI layer is easier.
        mFlickrImageListLiveData = new MediatorLiveData<>();
        mFlickrImageListLiveData.addSource(mRepository.getFlickrImages(), (resource) -> {
            mFlickrImageListLiveData.setValue(resource.getData());
            mFetchErrorLiveData.setValue(resource.getErrorMessage() != null);
            mIsLoadingLiveData.setValue((resource.getLoadingStatus() == LOADING));
        });
    }

    /**
     * Get a LiveData object that can be observed to get changes in the list of Flicker Images.
     */
    public LiveData<PagedList<FlickrImageEntity>> getFlickrImages() {
        return mFlickrImageListLiveData;
    }

    /**
     * Get a LiveData object that can be observed to get changes when there is an error loading the images.
     */
    public LiveData<Boolean> hasError() {
        return mFetchErrorLiveData;
    }

    /**
     * Get a LiveData object that can be observed to get changes in the the loading status.
     */
    public LiveData<Boolean> isLoading() {
        return mIsLoadingLiveData;
    }

    /**
     * Set the string that will be used to search for Flickr images.
     */
    public void setSearchString(String searchString) {
        mSearchStringLiveData.setValue(searchString);
    }

    /**
     * Search for Flickr images using the search string provided by {@link #setSearchString(String)}.
     */
    public void searchImages() {
        mRepository.searchImages(mSearchStringLiveData.getValue());
    }

}
