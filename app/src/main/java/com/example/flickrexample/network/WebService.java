package com.example.flickrexample.network;

import com.example.flickrexample.AppExecutors;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is responsible for communicating with a JSON API.
 * Normally I'd use something like Retrofit to perform the network requests.
 */
public abstract class WebService {

    private static final int REQUEST_TIMEOUT = 5000;

    private final AppExecutors mAppExecutors;

    WebService(final AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
    }

    void executeRequest(String requestPath, final WebServiceCallback<JSONObject> callback) {
        mAppExecutors.networkIO().execute(() -> {
            try {
                URL url = new URL(requestPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(REQUEST_TIMEOUT);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                JSONObject finalJsonResponse = new JSONObject(sb.toString());
                mAppExecutors.mainThread().execute(() -> {
                    callback.onResponse(finalJsonResponse);
                });
            } catch (JSONException | IOException e) {
                handleError(callback);
            }
        });
    }

    private void handleError(final WebServiceCallback callback) {
        mAppExecutors.mainThread().execute(() -> {
            callback.onFailure("Request failed");
        });
    }

    public interface WebServiceCallback<T> {
        void onResponse(T response);

        void onFailure(String errorMessage);
    }
}
