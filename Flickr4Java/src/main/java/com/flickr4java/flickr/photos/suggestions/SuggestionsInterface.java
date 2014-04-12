/**
 * @author acaplan
 */
package com.flickr4java.flickr.photos.suggestions;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author acaplan
 * 
 */
public class SuggestionsInterface {

    public static final String METHOD_APPROVE_SUGGESTION = "flickr.photos.suggestions.approveSuggestion";

    public static final String METHOD_REJECT_SUGGESTION = "flickr.photos.suggestions.rejectSuggestion";

    public static final String METHOD_REMOVE_SUGGESTION = "flickr.photos.suggestions.removeSuggestion";

    public static final String METHOD_SUGGEST_LOCATION = "flickr.photos.suggestions.suggestLocation";

    public static final String METHOD_GET_LIST = "flickr.photos.suggestions.getList";

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

    public SuggestionsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    public void approveSuggestion(String suggestionId) throws FlickrException {
        act(suggestionId, METHOD_APPROVE_SUGGESTION);
    }

    public void rejectSuggestion(String suggestionId) throws FlickrException {
        act(suggestionId, METHOD_REJECT_SUGGESTION);
    }

    public void removeSuggestion(String suggestionId) throws FlickrException {
        act(suggestionId, METHOD_REMOVE_SUGGESTION);
    }

    private void act(String suggestionId, String method) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", method);
        parameters.put("suggestion_id", suggestionId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void suggestLocation(String photoId, double lat, double lon, int accuracy, String woe_id, String place_id, String note) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SUGGEST_LOCATION);
        parameters.put("photo_id", photoId);
        parameters.put("lat", lat);
        parameters.put("lon", lon);
        if (accuracy > 0 && accuracy <= 16) {
            parameters.put("accuracy", accuracy);
        }
        if (woe_id != null) {
            parameters.put("woe_id", woe_id);
        }
        if (place_id != null) {
            parameters.put("place_id", place_id);
        }
        if (note != null) {
            parameters.put("note", note);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public List getList(String photoId, String statusId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);
        parameters.put("photo_id", photoId);
        parameters.put("status_id", statusId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element suggestions = response.getPayload();

        return null;
    }

}
