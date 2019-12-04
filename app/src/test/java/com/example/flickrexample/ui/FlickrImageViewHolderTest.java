package com.example.flickrexample.ui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import com.example.flickrexample.databinding.ListItemFlickrImageBinding;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.model.FlickrImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlickrImageViewHolderTest {

    @Mock
    private ListItemFlickrImageBinding mBinding;

    @Mock
    private AsyncTask<FlickrImage, Void, Bitmap> mDownloadImageTask;

    @Before
    public void setup() {
        when(mBinding.getRoot()).thenReturn(new LinearLayout(null));
    }

    @Test
    public void loadImage_ShouldExecuteTheAsyncTask() {
        FlickrImageEntity flickrImageEntity = new FlickrImageEntity();
        FlickrImageViewHolder holder = new FlickrImageViewHolder(mBinding);

        holder.loadImage(flickrImageEntity, mDownloadImageTask);

        Mockito.verify(mDownloadImageTask, times(1)).execute(flickrImageEntity);
    }

    @Test
    public void cancelLoadImage_WhenThereIsNoTask_ShouldNotThrowAnException() {
        FlickrImageEntity flickrImageEntity = new FlickrImageEntity();
        FlickrImageViewHolder holder = new FlickrImageViewHolder(mBinding);
        holder.loadImage(flickrImageEntity, mDownloadImageTask);

        holder.cancelLoadImage();
    }

    @Test
    public void cancelLoadImage_WhenThereIsATask_ShouldCancelTheTask() {
        FlickrImageEntity flickrImageEntity = new FlickrImageEntity();
        FlickrImageViewHolder holder = new FlickrImageViewHolder(mBinding);
        holder.loadImage(flickrImageEntity, mDownloadImageTask);

        holder.cancelLoadImage();

        Mockito.verify(mDownloadImageTask, times(1)).cancel(true);
    }
}
