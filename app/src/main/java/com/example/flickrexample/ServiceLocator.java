package com.example.flickrexample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.VisibleForTesting;

import com.example.flickrexample.db.FlickrDatabase;
import com.example.flickrexample.network.FlickrWebService;
import com.example.flickrexample.repository.FlickrImageRepository;

/**
 * Normally I wouldn't use a ServiceLocator pattern as I'd use Dagger to manage the dependency graph.
 * Given that I'm not using third party libraries this is a way to achieve some of the same goals.
 *
 * The idea behind this class is to create and store dependencies and then provide them on demand
 * in order to improve decoupling of classes from concrete dependencies.
 */
public class ServiceLocator implements ServiceLocatorInterface {

    private Application mApp;
    private AppExecutors mExecutors;
    private static ServiceLocatorInterface sInstance;

    private ServiceLocator(Context context) {
        this.mApp = (Application) context.getApplicationContext();
        this.mExecutors = new AppExecutors();
    }

    public static ServiceLocatorInterface getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ServiceLocator.class) {
                if (sInstance == null) {
                    sInstance = new ServiceLocator(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public FlickrImageRepository getRepository(int pageSize) {
        return FlickrImageRepository.getInstance(pageSize, getDatabase(), getExecutors(), getFlickrWebService());
    }

    @Override
    public FlickrWebService getFlickrWebService() {
        return new FlickrWebService(getExecutors());
    }

    @Override
    public FlickrDatabase getDatabase() {
        return FlickrDatabase.getInstance(mApp);
    }

    @Override
    public AppExecutors getExecutors() {
        return mExecutors;
    }

    @VisibleForTesting
    public static void swap(ServiceLocatorInterface serviceLocator) {
        sInstance = serviceLocator;
    }
}
