package com.example.flickrexample.ui;

import com.example.flickrexample.db.entity.FlickrImageEntity;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FlickrImageDiffUtilItemCallbackTest {

    FlickrImageDiffUtilItemCallback mCallback;

    @Before
    public void setup() {
        mCallback = new FlickrImageDiffUtilItemCallback();
    }

    @Test
    public void areItemsTheSame_WhenTheItemsAreNotTheSame_ReturnsFalse() {
        FlickrImageEntity oldItem = new FlickrImageEntity();
        oldItem.setId(1);
        FlickrImageEntity newItem = new FlickrImageEntity();
        newItem.setId(2);

        boolean areTheSame = mCallback.areItemsTheSame(oldItem, newItem);

        assertFalse(areTheSame);
    }

    @Test
    public void areItemsTheSame_WhenTheItemsAreTheSame_ReturnsTrue() {
        FlickrImageEntity oldItem = new FlickrImageEntity();
        oldItem.setId(1);
        FlickrImageEntity newItem = new FlickrImageEntity();
        newItem.setId(1);

        boolean areTheSame = mCallback.areItemsTheSame(oldItem, newItem);

        assertTrue(areTheSame);
    }

    @Test
    public void areContentsTheSame_WhenTheItemsAreNotTheSame_ReturnsFalse() {
        FlickrImageEntity oldItem = new FlickrImageEntity("ID1", "Title1", "server", "secret", "Kittens", 10, 1);
        FlickrImageEntity newItem = new FlickrImageEntity("ID2", "Title2", "server", "secret", "Kittens", 10, 1);

        boolean areTheSame = mCallback.areContentsTheSame(oldItem, newItem);

        assertFalse(areTheSame);
    }

    @Test
    public void areContentsTheSame_WhenTheItemsAreTheSame_ReturnsTrue() {
        FlickrImageEntity oldItem = new FlickrImageEntity("ID", "Title", "server", "secret", "Kittens", 10, 1);
        FlickrImageEntity newItem = new FlickrImageEntity("ID", "Title", "server", "secret", "Kittens", 10, 1);

        boolean areTheSame = mCallback.areContentsTheSame(oldItem, newItem);

        assertTrue(areTheSame);
    }
}
