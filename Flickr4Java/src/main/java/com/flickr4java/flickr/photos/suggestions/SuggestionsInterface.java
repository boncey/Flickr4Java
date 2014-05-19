/**
 * @author acaplan
 */
package com.flickr4java.flickr.photos.suggestions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.places.Location;
import com.flickr4java.flickr.places.Place;
import com.flickr4java.flickr.util.XMLUtilities;

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

    public SuggestionList<Suggestion> getList(String photoId) throws FlickrException {
    	SuggestionList<Suggestion> suggestionList = new SuggestionList<Suggestion>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        parameters.put("photo_id", photoId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element suggestionsElement = response.getPayload();
        
        suggestionList.setPage(XMLUtilities.getIntAttribute(suggestionsElement, "page"));
        suggestionList.setPerPage(XMLUtilities.getIntAttribute(suggestionsElement, "perpage"));
        suggestionList.setTotal(XMLUtilities.getIntAttribute(suggestionsElement, "total"));
        
        NodeList suggestionNodes = suggestionsElement.getElementsByTagName("suggestion");
        for (int i = 0; i < suggestionNodes.getLength(); i++) {
            Element suggestionElement = (Element) suggestionNodes.item(i);
            Suggestion suggestion = new Suggestion();
            suggestion.setSuggestionId(suggestionElement.getAttribute("id"));
            suggestion.setPhotoId(suggestionElement.getAttribute("photo_id"));
            suggestion.setDateSuggested(suggestionElement.getAttribute("date_suggested"));
            suggestion.setNote(XMLUtilities.getChild(suggestionElement, "note").getTextContent());
            
            Element suggestedElement = XMLUtilities.getChild(suggestionElement, "suggested_by");
            suggestion.setSuggestorUsername(suggestedElement.getAttribute("username"));
            suggestion.setSuggestorId(suggestedElement.getAttribute("nsid"));
            
            Element locationElement = XMLUtilities.getChild(suggestionElement, "location");
            Location location = new Location();
            location.setLatitude(Double.parseDouble(locationElement.getAttribute("latitude")));
            location.setLongitude(Double.parseDouble(locationElement.getAttribute("longitude")));
            location.setWoeId(locationElement.getAttribute("woeid"));
            location.setAccuracy(Integer.parseInt(locationElement.getAttribute("accuracy")));
            
            Element regionElement = XMLUtilities.getChild(locationElement, "region");
            if(regionElement != null){
	            Place placeReg = new Place();
	            placeReg.setPlaceId(regionElement.getAttribute("place_id"));
	            placeReg.setWoeId(regionElement.getAttribute("woeid"));
	            location.setRegion(placeReg);
            }
            
            Element countryElement = XMLUtilities.getChild(locationElement, "country");
            if(countryElement != null){
	            Place placeCtry = new Place();
	            placeCtry.setPlaceId(countryElement.getAttribute("place_id"));
	            placeCtry.setWoeId(countryElement.getAttribute("woeid"));
	            location.setCountry(placeCtry);
            }

            Element countyElement = XMLUtilities.getChild(locationElement, "county");
            if(countyElement != null){
	            Place placeCnty = new Place();
	            placeCnty.setPlaceId(countyElement.getAttribute("place_id"));
	            placeCnty.setWoeId(countyElement.getAttribute("woeid"));
	            location.setCounty(placeCnty);
            }
            
            suggestion.setLocation(location);
            suggestionList.add(suggestion);
        }
        return suggestionList;

    }

}
