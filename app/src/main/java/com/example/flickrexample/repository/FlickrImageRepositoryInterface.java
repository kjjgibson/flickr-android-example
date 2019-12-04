package com.example.flickrexample.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.flickrexample.db.entity.FlickrImageEntity;

public interface FlickrImageRepositoryInterface {

    void searchImages(String searchString);
    LiveData<Resource<PagedList<FlickrImageEntity>>> getFlickrImages();
    void fetchNewFlickrImages(final String searchString, int pageNumber);

}
