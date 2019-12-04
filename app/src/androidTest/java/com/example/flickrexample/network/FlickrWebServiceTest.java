package com.example.flickrexample.network;

import com.example.flickrexample.AppExecutors;
import com.example.flickrexample.db.entity.FlickrImageEntity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class FlickrWebServiceTest {

    @Test
    public void getPageUrl_ReturnsTheUrl() {
        String url = FlickrWebService.getPageUrl("kittens", 1);

        assertEquals("https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=kittens&per_page=100&page=1", url);
    }

    @Test
    public void getImageUrl_ReturnsTheUrl() {
        FlickrImageEntity flickrImageEntity = new FlickrImageEntity("ID", "Kittens Title", "server", "secret", "Kittens", 10, 1);

        String url = FlickrWebService.getImageUrl(flickrImageEntity);

        assertEquals("https://farm10.static.flickr.com/server/ID_secret.jpg", url);
    }

    @Test
    public void getFlickrImages_WhenTheRequestSucceeds_CallsTheCallbackOnResponseMethod() {
        FlickrWebService webService = new MockSuccessfulFlickrWebService(null);

        webService.getFlickrImages("kittens", 1, new WebService.WebServiceCallback<List<FlickrImageEntity>>() {
            @Override
            public void onResponse(List<FlickrImageEntity> response) {
                assertEquals(1, response.size());
                assertNotNull(response.get(0));
                assertEquals("flickrId", response.get(0).getFlickrId());
            }

            @Override
            public void onFailure(String errorMessage) {
                Assert.fail("The mocked request failed: " + errorMessage);
            }
        });
    }

    @Test
    public void getFlickrImages_WhenTheRequestFails_CallsTheCallbackOnFailureMethod() {
        FlickrWebService webService = new MockFailureFlickrWebService(null);

        webService.getFlickrImages("kittens", 1, new WebService.WebServiceCallback<List<FlickrImageEntity>>() {
            @Override
            public void onResponse(List<FlickrImageEntity> response) {
                Assert.fail("The mocked request succeeds but was expected to fail");
            }

            @Override
            public void onFailure(String errorMessage) {
                assertEquals("Error!", errorMessage);
            }
        });
    }

    class MockSuccessfulFlickrWebService extends FlickrWebService {

        MockSuccessfulFlickrWebService(AppExecutors appExecutors) {
            super(appExecutors);
        }

        @Override
        void executeRequest(String requestPath, WebServiceCallback<JSONObject> callback) {
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject("{ \"photos\": {" +
                        "    \"page\": 2," +
                        "    \"pages\": 100," +
                        "    \"perpage\": 100," +
                        "    \"total\": \"2000\"," +
                        "    \"photo\": [" +
                        "      {" +
                        "        \"id\": \"flickrId\"," +
                        "        \"secret\": \"secret\"," +
                        "        \"server\": \"server\"," +
                        "        \"farm\": 66," +
                        "        \"title\": \"title\"" +
                        "      }]" +
                        "}}");
            } catch (JSONException e) {
                Assert.fail("Test data not setup correctly");
            }

            callback.onResponse(jsonObject);
        }
    }

    class MockFailureFlickrWebService extends FlickrWebService {

        MockFailureFlickrWebService(AppExecutors appExecutors) {
            super(appExecutors);
        }

        @Override
        void executeRequest(String requestPath, WebServiceCallback<JSONObject> callback) {
            callback.onFailure("Error!");
        }
    }
}
