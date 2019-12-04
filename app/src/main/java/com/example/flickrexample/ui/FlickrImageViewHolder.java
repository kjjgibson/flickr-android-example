package com.example.flickrexample.ui;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrexample.databinding.ListItemFlickrImageBinding;
import com.example.flickrexample.model.FlickrImage;

class FlickrImageViewHolder extends RecyclerView.ViewHolder {

    private final ListItemFlickrImageBinding mBinding;

    @Nullable
    private AsyncTask<FlickrImage, Void, Bitmap> mAsyncTask;

    FlickrImageViewHolder(final ListItemFlickrImageBinding binding) {
        super(binding.getRoot());

        this.mBinding = binding;
    }

    ListItemFlickrImageBinding getBinding() {
        return mBinding;
    }

    /**
     * Load an image for the FlickrImage using the task provided.
     */
    void loadImage(final FlickrImage flickrImage, final AsyncTask<FlickrImage, Void, Bitmap> asyncTask) {
        this.mAsyncTask = asyncTask;

        mAsyncTask.execute(flickrImage);
    }

    /**
     * Cancel any potentially running AsyncTask that may be downloading an image for a previous use of this ViewHolder.
     */
    void cancelLoadImage() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
    }
}
