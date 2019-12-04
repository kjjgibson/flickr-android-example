package com.example.flickrexample.network;

import com.example.flickrexample.db.entity.FlickrImageEntity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class FlickrImageListResponseDeserializerTest {

    private FlickrImageListResponseDeserializer mDeserializer;

    @Before
    public void setup() {
        mDeserializer = new FlickrImageListResponseDeserializer();
    }

    @Test
    public void deserialize_WithValidJson_ReturnsListOfEntities() throws JSONException {
        JSONObject jsonObject = new JSONObject("{ \"photos\": {\n" +
                "    \"page\": 2,\n" +
                "    \"pages\": 100,\n" +
                "    \"perpage\": 100,\n" +
                "    \"total\": \"2000\",\n" +
                "    \"photo\": [\n" +
                "      {\n" +
                "        \"id\": \"flickrId\",\n" +
                "        \"secret\": \"secret\",\n" +
                "        \"server\": \"server\",\n" +
                "        \"farm\": 66,\n" +
                "        \"title\": \"title\"\n" +
                "      }]" +
                "}}");

        List<FlickrImageEntity> flickrImageEntities = mDeserializer.deserialize(jsonObject);
        FlickrImageEntity flickrImageEntity = flickrImageEntities.get(0);

        assertEquals("flickrId", flickrImageEntity.getFlickrId());
        assertEquals("secret", flickrImageEntity.getSecret());
        assertEquals("server", flickrImageEntity.getServer());
        assertEquals(66, flickrImageEntity.getFarm());
        assertEquals("title", flickrImageEntity.getTitle());
        assertEquals(2, flickrImageEntity.getPageNumber());
    }

    @Test
    public void deserialize_WithInvalidJson_ThrowsAnException() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{}");
        } catch (JSONException e) {
            Assert.fail("Did not set up JSON object correctly");
        }

        try {
            mDeserializer.deserialize(jsonObject);
            Assert.fail("Expected exception to be thrown");
        } catch (JSONException e) {
            // Expected exception
        }
    }
}
