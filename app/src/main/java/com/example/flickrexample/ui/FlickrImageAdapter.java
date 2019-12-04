package com.example.flickrexample.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.flickrexample.R;
import com.example.flickrexample.databinding.ListItemFlickrImageBinding;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.model.FlickrImage;
import com.example.flickrexample.network.DownloadFlickrImageTask;

public class FlickrImageAdapter extends PagedListAdapter<FlickrImageEntity, FlickrImageViewHolder> {

    private static final DiffUtil.ItemCallback<FlickrImageEntity> DIFF_CALLBACK = new FlickrImageDiffUtilItemCallback();

    FlickrImageAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemFlickrImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_flickr_image, parent, false);
        return new FlickrImageViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        FlickrImage flickrImage = getItem(position);

        // Cancel any potential image loading task if this ViewHolder is going to be reused.
        // There's not point in continuing to load an image if it's off screen (unless potentially if we want to cache it later on)
        holder.cancelLoadImage();

        if (flickrImage != null) {
            holder.getBinding().setFlickrImage(flickrImage);
            holder.getBinding().imageViewImage.setImageBitmap(null); // Clear the image straight away so that you don't see the previously loaded image
            holder.loadImage(flickrImage, new DownloadFlickrImageTask(holder.getBinding().imageViewImage));
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

}

