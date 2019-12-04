package com.example.flickrexample.repository;

import com.example.flickrexample.db.entity.FlickrImageEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PagedListBoundaryCallbackTest {

    @Mock
    private FlickrImageRepository mMockRepository;

    @Test
    public void onZeroItemsLoaded_FetchesTheFirstPage() {
        PagedListBoundaryCallback callback = new PagedListBoundaryCallback("Kittens" , mMockRepository);

        callback.onZeroItemsLoaded();

        Mockito.verify(mMockRepository, times(1)).fetchNewFlickrImages("Kittens" , 1);
    }

    @Test
    public void onItemAtEndLoaded_FetchesTheNextPage() {
        PagedListBoundaryCallback callback = new PagedListBoundaryCallback("Kittens" , mMockRepository);
        FlickrImageEntity itemAtEnd = new FlickrImageEntity();
        itemAtEnd.setPageNumber(1);

        callback.onItemAtEndLoaded(itemAtEnd);

        Mockito.verify(mMockRepository, times(1)).fetchNewFlickrImages("Kittens" , 2);
    }
}
