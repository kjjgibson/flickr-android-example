package com.example.flickrexample.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.example.flickrexample.ServiceLocator;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.repository.FlickrImageRepository;
import com.example.flickrexample.repository.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private MutableLiveData<Resource<PagedList<FlickrImageEntity>>> mFlickrImageResources;

    @Mock
    private Application mMockApplication;

    @Mock
    private FlickrImageRepository mMockRepository;

    @Mock
    private ServiceLocator mMockServiceLocator;

    @Mock
    private PagedList<FlickrImageEntity> mMockPagedList;

    @Before
    public void setup() {
        mFlickrImageResources = new MutableLiveData<>();

        ServiceLocator.swap(mMockServiceLocator);

        when(mMockServiceLocator.getRepository(100)).thenReturn(mMockRepository);
        when(mMockRepository.getFlickrImages()).thenReturn(mFlickrImageResources);
    }

    @Test
    public void getFlickrImages_ReturnsALiveData() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);

        LiveData<PagedList<FlickrImageEntity>> flickrImages = viewModel.getFlickrImages();

        assertNotNull(flickrImages);
    }

    @Test
    public void getFlickrImages_DefaultValueIsNull() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);

        LiveData<PagedList<FlickrImageEntity>> flickrImages = viewModel.getFlickrImages();
        flickrImages.observeForever(flickrImageEntities -> { });

        assertNull(flickrImages.getValue());
    }

    @Test
    public void getFlickrImages_SetsTheCorrectValue() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);
        mFlickrImageResources.setValue(Resource.success(mMockPagedList));

        LiveData<PagedList<FlickrImageEntity>> flickrImages = viewModel.getFlickrImages();
        flickrImages.observeForever(flickrImageEntities -> { });

        assertEquals(mMockPagedList, flickrImages.getValue());
    }

    @Test
    public void isLoading_DefaultValueIsFalse() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);

        LiveData<Boolean> isLoading = viewModel.isLoading();
        isLoading.observeForever(flickrImageEntities -> { });

        assertFalse(isLoading.getValue());
    }

    @Test
    public void isLoading_SetsTheCorrectValue() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);
        viewModel.getFlickrImages().observeForever(flickrImageEntities -> { });
        mFlickrImageResources.setValue(Resource.loading(null));

        LiveData<Boolean> isLoading = viewModel.isLoading();
        isLoading.observeForever(flickrImageEntities -> { });

        assertTrue(isLoading.getValue());
    }

    @Test
    public void hasError_SetsTheCorrectValue() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);
        viewModel.getFlickrImages().observeForever(flickrImageEntities -> { });
        mFlickrImageResources.setValue(Resource.error("Error!", null));

        LiveData<Boolean> hasError = viewModel.hasError();

        assertTrue(hasError.getValue());
    }

    @Test
    public void searchImages_DelegatesToTheRepository() {
        MainViewModel viewModel = new MainViewModel(mMockApplication);

        viewModel.setSearchString("Kittens");
        viewModel.searchImages();

        Mockito.verify(mMockRepository, times(1)).searchImages("Kittens" );
    }

}
