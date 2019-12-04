package com.example.flickrexample.network;

import com.example.flickrexample.db.entity.FlickrImageEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Deserialize the response from the Flickr API into a list of entities.
 * Normally I would use something like GSON to handle this using annotated classes.
 */
class FlickrImageListResponseDeserializer implements ResponseDeserializer<List<FlickrImageEntity>> {

    public List<FlickrImageEntity> deserialize(final JSONObject response) throws JSONException {
        ArrayList<FlickrImageEntity> flickrImageEntities = new ArrayList<>();

        JSONObject photosContainer = response.getJSONObject("photos");
        JSONArray jsonArray = photosContainer.getJSONArray("photo");
        int pageNumber = photosContainer.getInt("page");

        for (int i = 0; i < jsonArray.length(); i++) {
            flickrImageEntities.add(deserializeEntity(jsonArray.getJSONObject(i), pageNumber));
        }

        return flickrImageEntities;
    }

    private FlickrImageEntity deserializeEntity(final JSONObject entityObject, int pageNumber) throws JSONException {
        FlickrImageEntity entity = new FlickrImageEntity();

        entity.setFlickrId(entityObject.getString("id"));
        entity.setTitle(entityObject.getString("title"));
        entity.setFarm(entityObject.getInt("farm"));
        entity.setServer(entityObject.getString("server"));
        entity.setSecret(entityObject.getString("secret"));
        entity.setPageNumber(pageNumber);

        return entity;
    }

}
