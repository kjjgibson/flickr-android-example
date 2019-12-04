package com.example.flickrexample.repository;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ResourceTest {

    @Test
    public void success_ReturnsANewResource() {
        String data = "Data";

        Resource resource = Resource.success(data);

        assertEquals(LoadingStatus.SUCCESS, resource.getLoadingStatus());
        assertEquals(data, resource.getData());
    }

    @Test
    public void error_ReturnsANewResource() {
        String data = "Data";
        String errorMessage = "Error!";

        Resource resource = Resource.error(errorMessage, data);

        assertEquals(LoadingStatus.ERROR, resource.getLoadingStatus());
        assertEquals(data, resource.getData());
        assertEquals(errorMessage, resource.getErrorMessage());
    }

    @Test
    public void loading_ReturnsANewResource() {
        String data = "Data";

        Resource resource = Resource.loading(data);

        assertEquals(LoadingStatus.LOADING, resource.getLoadingStatus());
        assertEquals(data, resource.getData());
    }
}
