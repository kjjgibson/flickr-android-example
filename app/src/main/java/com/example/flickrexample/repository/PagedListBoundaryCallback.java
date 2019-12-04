package com.example.flickrexample.repository;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;

import com.example.flickrexample.db.entity.FlickrImageEntity;

/**
 * This callback is used by the LivePagedListBuilder.
 * The methods are called when the list thinks that more data should be loaded.
 * The repository is then used to trigger fetching new data from the WebService.
 */
public class PagedListBoundaryCallback extends PagedList.BoundaryCallback<FlickrImageEntity> {

    private final String mSearchString;
    private final FlickrImageRepositoryInterface mRepository;

    PagedListBoundaryCallback(String searchString, final FlickrImageRepositoryInterface repository) {
        this.mSearchString = searchString;
        this.mRepository = repository;
    }

    /**
     * When the list first loads the data source and there are no items then we should load the first page.
     */
    @Override
    public void onZeroItemsLoaded() {
        super.onZeroItemsLoaded();

        mRepository.fetchNewFlickrImages(mSearchString, 1);
    }

    /**
     * When the user scrolls to the end of the non-empty list then we should fetch the next page.
     */
    @Override
    public void onItemAtEndLoaded(@NonNull FlickrImageEntity itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);

        mRepository.fetchNewFlickrImages(mSearchString, itemAtEnd.getPageNumber() + 1);
    }

}
