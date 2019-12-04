package com.example.flickrexample.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.flickrexample.db.entity.FlickrImageEntity;

/**
 * A diff callback used by the PagedListAdapter in order to figure out the position of any new items.
 */
public class FlickrImageDiffUtilItemCallback extends DiffUtil.ItemCallback<FlickrImageEntity> {

    @Override
    public boolean areItemsTheSame(@NonNull FlickrImageEntity oldItem, @NonNull FlickrImageEntity newItem) {
        return newItem.getId() == oldItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull FlickrImageEntity oldItem, @NonNull FlickrImageEntity newItem) {
        return oldItem.equals(newItem);
    }

}
