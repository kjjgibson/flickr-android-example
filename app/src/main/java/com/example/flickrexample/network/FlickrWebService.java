package com.example.flickrexample.network;

import android.net.Uri;

import com.example.flickrexample.AppExecutors;
import com.example.flickrexample.db.entity.FlickrImageEntity;
import com.example.flickrexample.model.FlickrImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * This class is responsible for communicating with the Flickr API.
 * Normally I'd use something like Retrofit to perform the network requests.
 */
public class FlickrWebService extends WebService {

    private static final String AUTHORITY = "api.flickr.com";
    // Not the best idea to put this here
    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";

    public FlickrWebService(final AppExecutors appExecutors) {
        super(appExecutors);
    }

    /**
     * Construct the URL used to load a page of image data from the Flickr API.
     */
    static String getPageUrl(String searchString, int pageNumber) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(AUTHORITY)
                .appendPath("services")
                .appendPath("rest")
                .appendQueryParameter("method", "flickr.photos.search")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("safe_search", "1")
                .appendQueryParameter("text", searchString)
                .appendQueryParameter("per_page", "100")
                .appendQueryParameter("page", "" + pageNumber);

        return builder.build().toString();
    }

    /**
     * Construct the URL used to load a specific image from the Flickr API.
     * The image URL is made up using parts of the FlickrImage resource returned by the page request.
     */
    static String getImageUrl(final FlickrImage flickrImage) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("farm" + flickrImage.getFarm() + ".static.flickr.com")
                .appendPath(flickrImage.getServer())
                .appendPath(flickrImage.getFlickrId() + "_" + flickrImage.getSecret() + ".jpg");

        return builder.build().toString();
    }


    /**
     * Fetch a page of image data from the Flickr API.
     *
     * @param searchString - A search string to find images
     * @param pageNumber - The page offset to fetch
     * @param callback - The callback will be called when the network request is complete
     */
    public void getFlickrImages(String searchString, int pageNumber, final WebServiceCallback<List<FlickrImageEntity>> callback) {
        executeRequest(
                getPageUrl(searchString, pageNumber),
                new WebServiceCallback<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        FlickrImageListResponseDeserializer deserializer = new FlickrImageListResponseDeserializer();
                        try {
                            callback.onResponse(deserializer.deserialize(response));
                        } catch (JSONException e) {
                            callback.onFailure("Cannot deserialize response");
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        callback.onFailure(errorMessage);
                    }
                }
        );
    }
}
