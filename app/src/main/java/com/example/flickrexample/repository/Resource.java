package com.example.flickrexample.repository;

import androidx.annotation.Nullable;

import static com.example.flickrexample.repository.LoadingStatus.ERROR;
import static com.example.flickrexample.repository.LoadingStatus.LOADING;
import static com.example.flickrexample.repository.LoadingStatus.SUCCESS;

/**
 * This class encapsulates data that is loaded from a data source.
 * Often the UI is interested in knowing about the loading and error statuses and so we bundle these
 * three things up into a single Resource class.
 */
public class Resource<T> {

    private final LoadingStatus mLoadingStatus;

    @Nullable
    private final T mData;

    @Nullable
    private final String mErrorMessage;

    private Resource(LoadingStatus loadingStatus, @Nullable T data, @Nullable String errorMessage) {
        this.mLoadingStatus = loadingStatus;
        this.mData = data;
        this.mErrorMessage = errorMessage;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String errorMessage, @Nullable T data) {
        return new Resource<>(ERROR, data, errorMessage);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public LoadingStatus getLoadingStatus() {
        return mLoadingStatus;
    }

    @Nullable
    public T getData() {
        return mData;
    }

    @Nullable
    public String getErrorMessage() {
        return mErrorMessage;
    }
}