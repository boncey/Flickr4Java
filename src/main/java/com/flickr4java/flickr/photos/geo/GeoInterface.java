package com.flickr4java.flickr.photos.geo;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Access to the flickr.photos.geo methods.
 * 
 * @author till (Till Krech - flickr:extranoise)
 * @version $Id: GeoInterface.java,v 1.5 2009/07/22 22:39:36 x-mago Exp $
 */
public class GeoInterface {
    public static final String METHOD_GET_LOCATION = "flickr.photos.geo.getLocation";

    public static final String METHOD_GET_PERMS = "flickr.photos.geo.getPerms";

    public static final String METHOD_REMOVE_LOCATION = "flickr.photos.geo.removeLocation";

    public static final String METHOD_SET_LOCATION = "flickr.photos.geo.setLocation";

    public static final String METHOD_SET_PERMS = "flickr.photos.geo.setPerms";

    public static final String METHOD_BATCH_CORRECT_LOCATION = "flickr.photos.geo.batchCorrectLocation";

    public static final String METHOD_CORRECT_LOCATION = "flickr.photos.geo.correctLocation";

    public static final String METHOD_PHOTOS_FOR_LOCATION = "flickr.photos.geo.photosForLocation";

    public static final String METHOD_SET_CONTEXT = "flickr.photos.geo.setContext";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transport;

    public GeoInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the geo data (latitude and longitude and the accuracy level) for a photo.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            reqired photo id, not null
     * @return Geo Data, if the photo has it.
     * @throws FlickrException
     *             if photo id is invalid, if photo has no geodata or if any other error has been reported in the response.
     */
    public GeoData getLocation(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LOCATION);
        parameters.put("photo_id", photoId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // response:
        // <photo id="123">
        // <location latitude="-17.685895" longitude="-63.36914" accuracy="6" />
        // </photo>

        Element photoElement = response.getPayload();

        Element locationElement = XMLUtilities.getChild(photoElement, "location");
        String latStr = locationElement.getAttribute("latitude");
        String lonStr = locationElement.getAttribute("longitude");
        String accStr = locationElement.getAttribute("accuracy");
        // I ignore the id attribute. should be the same as the given
        // photo id.
        GeoData geoData = new GeoData(lonStr, latStr, accStr);
        return geoData;
    }

    /**
     * Get permissions for who may view geo data for a photo.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param photoId
     *            reqired photo id, not null
     * @return the permissions
     * @throws FlickrException
     *             if photo id is invalid, if photo has no geodata or if any other error has been reported in the response.
     */
    public GeoPermissions getPerms(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PERMS);
        parameters.put("photo_id", photoId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // response:
        // <perms id="240935723" ispublic="1" iscontact="0" isfriend="0" isfamily="0"/>
        GeoPermissions perms = new GeoPermissions();
        Element permsElement = response.getPayload();
        perms.setPublic("1".equals(permsElement.getAttribute("ispublic")));
        perms.setContact("1".equals(permsElement.getAttribute("iscontact")));
        perms.setFriend("1".equals(permsElement.getAttribute("isfriend")));
        perms.setFamily("1".equals(permsElement.getAttribute("isfamily")));
        perms.setId(permsElement.getAttribute("id"));
        // I ignore the id attribute. should be the same as the given
        // photo id.
        return perms;
    }

    /**
     * Removes the geo data associated with a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @throws FlickrException
     */
    public void removeLocation(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REMOVE_LOCATION);
        parameters.put("photo_id", photoId);

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        // This method has no specific response - It returns an empty sucess response
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Sets the geo data (latitude and longitude and, optionally, the accuracy level) for a photo. Before users may assign location data to a photo they must
     * define who, by default, may view that information. Users can edit this preference at <a href="http://www.flickr.com/account/geo/privacy/">flickr</a>. If
     * a user has not set this preference, the API method will return an error.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The id of the photo to cet permissions for.
     * @param location
     *            geo data with optional accuracy (1-16), accuracy 0 to use the default.
     * @throws FlickrException
     */
    public void setLocation(String photoId, GeoData location) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_LOCATION);

        parameters.put("photo_id", photoId);
        parameters.put("lat", String.valueOf(location.getLatitude()));
        parameters.put("lon", String.valueOf(location.getLongitude()));
        int accuracy = location.getAccuracy();
        if (accuracy > 0) {
            parameters.put("accuracy", String.valueOf(location.getAccuracy()));
        }

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        // This method has no specific response - It returns an empty sucess response
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the permission for who may view the geo data associated with a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The id of the photo to set permissions for.
     * @param perms
     *            Permissions, who can see the geo data of this photo
     * @throws FlickrException
     */
    public void setPerms(String photoId, GeoPermissions perms) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_PERMS);
        parameters.put("photo_id", photoId);
        parameters.put("is_public", perms.isPublic() ? "1" : "0");
        parameters.put("is_contact", perms.isContact() ? "1" : "0");
        parameters.put("is_friend", perms.isFriend() ? "1" : "0");
        parameters.put("is_family", perms.isFamily() ? "1" : "0");

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        // This method has no specific response - It returns an empty sucess response
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Correct the places hierarchy for all the photos for a user at a given latitude, longitude and accuracy.
     * <p>
     * 
     * Batch corrections are processed in a delayed queue so it may take a few minutes before the changes are reflected in a user's photos.
     * 
     * @param location
     *            The latitude/longitude and accuracy of the photos to be update.
     * @param placeId
     *            A Flickr Places ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId
     *            A Where On Earth (WOE) ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @throws FlickrException
     */
    public void batchCorrectLocation(GeoData location, String placeId, String woeId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_BATCH_CORRECT_LOCATION);

        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }
        parameters.put("lat", Float.toString(location.getLatitude()));
        parameters.put("lon", Float.toString(location.getLongitude()));
        parameters.put("accuracy", Integer.toString(location.getAccuracy()));

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        // This method has no specific response - It returns an empty sucess response
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * 
     * @param photoId
     *            Photo id (required).
     * @param placeId
     *            A Flickr Places ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId
     *            A Where On Earth (WOE) ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @throws FlickrException
     */
    public void correctLocation(String photoId, String placeId, String woeId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_CORRECT_LOCATION);

        parameters.put("photo_id", photoId);
        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        // This method has no specific response - It returns an empty sucess response
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Return a list of photos for a user at a specific latitude, longitude and accuracy.
     * 
     * @param location
     * @param extras
     * @param perPage
     * @param page
     * @return The collection of Photo objects
     * @throws FlickrException
     * @see com.flickr4java.flickr.photos.Extras
     */
    public PhotoList<Photo> photosForLocation(GeoData location, Set<String> extras, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PhotoList<Photo> photos = new PhotoList<Photo>();
        parameters.put("method", METHOD_PHOTOS_FOR_LOCATION);

        if (extras.size() > 0) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }
        parameters.put("lat", Float.toString(location.getLatitude()));
        parameters.put("lon", Float.toString(location.getLongitude()));
        parameters.put("accuracy", Integer.toString(location.getAccuracy()));
        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoElements = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoElements.getLength(); i++) {
            Element photoElement = (Element) photoElements.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Indicate the state of a photo's geotagginess beyond latitude and longitude.
     * <p>
     * 
     * Note : photos passed to this method must already be geotagged (using the {@link GeoInterface#setLocation(String, GeoData)} method).
     * 
     * @param photoId
     *            Photo id (required).
     * @param context
     *            Context is a numeric value representing the photo's geotagginess beyond latitude and longitude. For example, you may wish to indicate that a
     *            photo was taken "indoors" (1) or "outdoors" (2).
     * @throws FlickrException
     */
    public void setContext(String photoId, int context) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_CONTEXT);

        parameters.put("photo_id", photoId);
        parameters.put("context", "" + context);

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        // This method has no specific response - It returns an empty sucess response
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
