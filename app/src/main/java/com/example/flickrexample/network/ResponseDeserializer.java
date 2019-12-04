package com.example.flickrexample.network;

import org.json.JSONException;
import org.json.JSONObject;

interface ResponseDeserializer<T> {

    T deserialize(JSONObject jsonObject) throws JSONException;

}
