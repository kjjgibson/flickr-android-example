package com.example.flickrexample;

import com.example.flickrexample.db.FlickrDatabase;
import com.example.flickrexample.network.FlickrWebService;
import com.example.flickrexample.repository.FlickrImageRepositoryInterface;

public interface ServiceLocatorInterface {
    FlickrImageRepositoryInterface getRepository(int pageSize);
    FlickrWebService getFlickrWebService();
    FlickrDatabase getDatabase();
    AppExecutors getExecutors();
}
