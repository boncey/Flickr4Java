package com.flickr4java.flickr.photos.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

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

    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    public GeoInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the geo data (latitude and longitude and the accuracy level) for a photo.
     *
     * This method does not require authentication.
     *
     * @param photoId reqired photo id, not null
     * @return Geo Data, if the photo has it. 
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException if photo id is invalid, if photo has no geodata 
     * or if any other error has been reported in the response.
     */
    public GeoData getLocation(String photoId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_LOCATION));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // response:
		// <photo id="123">
		//  <location latitude="-17.685895" longitude="-63.36914" accuracy="6" />
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
     * @param photoId reqired photo id, not null
     * @return the permissions
     * @throws SAXException
     * @throws IOException
     * @throws SAXException
     * @throws IOException
     * @throws FlickrException
     * @throws FlickrException if photo id is invalid, if photo has no geodata
     * or if any other error has been reported in the response.
     */
    public GeoPermissions getPerms(String photoId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_PERMS));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
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
        // I ignore the id attribute. should be the same as the given
        // photo id.
		return perms;
    }

    /**
     * Removes the geo data associated with a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @throws SAXException
     * @throws IOException
     * @throws FlickrException
     */
    public void removeLocation(String photoId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_REMOVE_LOCATION));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
        // This method has no specific response - It returns an empty sucess response 
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Sets the geo data (latitude and longitude and, optionally, the accuracy level) for a photo. 
     * Before users may assign location data to a photo they must define who, by default, 
     * may view that information. Users can edit this preference 
     * at <a href="http://www.flickr.com/account/geo/privacy/">flickr</a>. If a user has not set this preference, 
     * the API method will return an error.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The id of the photo to cet permissions for.
     * @param location geo data with optional accuracy (1-16), accuracy 0 to use the default.
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException 
     */
    public void setLocation(String photoId, GeoData location) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_LOCATION));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("lat", String.valueOf(location.getLatitude())));
        parameters.add(new Parameter("lon", String.valueOf(location.getLongitude())));
        int accuracy = location.getAccuracy();
        if (accuracy > 0) {
            parameters.add(new Parameter("accuracy", String.valueOf(location.getAccuracy())));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
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
     * @param photoId The id of the photo to set permissions for.
     * @param perms Permissions, who can see the geo data of this photo
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException 
     */
    public void setPerms(String photoId, GeoPermissions perms) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_PERMS));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("is_public", perms.isPublic() ? "1" : "0"));
        parameters.add(new Parameter("is_contact", perms.isContact() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", perms.isFriend() ? "1" : "0"));
        parameters.add(new Parameter("is_family", perms.isFamily() ? "1" : "0"));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
        // This method has no specific response - It returns an empty sucess response 
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Correct the places hierarchy for all the photos for a user at a given
     * latitude, longitude and accuracy.<p>
     *
     * Batch corrections are processed in a delayed queue so it may take a
     * few minutes before the changes are reflected in a user's photos.
     *
     * @param location The latitude/longitude and accuracy of the photos to be update.
     * @param placeId A Flickr Places ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId A Where On Earth (WOE) ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException 
     */
    public void batchCorrectLocation(
        GeoData location,
        String placeId,
        String woeId
    ) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_BATCH_CORRECT_LOCATION));
        parameters.add(new Parameter("api_key", apiKey));

        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        parameters.add(new Parameter("lat", location.getLatitude()));
        parameters.add(new Parameter("lon", location.getLongitude()));
        parameters.add(new Parameter("accuracy", location.getAccuracy()));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
        // This method has no specific response - It returns an empty sucess response 
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * 
     * @param photoId Photo id (required).
     * @param placeId A Flickr Places ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId A Where On Earth (WOE) ID. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void correctLocation(
        String photoId,
        String placeId,
        String woeId
    ) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_CORRECT_LOCATION));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
        // This method has no specific response - It returns an empty sucess response 
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Return a list of photos for a user at a specific
     * latitude, longitude and accuracy.
     *
     * @param location
     * @param extras
     * @param perPage
     * @param page
     * @return The collection of Photo objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     * @see com.flickr4java.flickr.test.photos.Extras
     */
    public PhotoList photosForLocation(
        GeoData location,
        Set extras,
        int perPage, int page
    ) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        PhotoList photos = new PhotoList();
        parameters.add(new Parameter("method", METHOD_PHOTOS_FOR_LOCATION));
        parameters.add(new Parameter("api_key", apiKey));

        if (extras.size() > 0) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(new Parameter("lat", location.getLatitude()));
        parameters.add(new Parameter("lon", location.getLongitude()));
        parameters.add(new Parameter("accuracy", location.getAccuracy()));
        Response response = transport.get(transport.getPath(), parameters);
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
     * Indicate the state of a photo's geotagginess beyond latitude and longitude.<p>
     *
     * Note : photos passed to this method must already be geotagged
     * (using the {@link GeoInterface#setLocation(String, GeoData)} method).
     *
     * @param photoId Photo id (required).
     * @param context Context is a numeric value representing the photo's geotagginess beyond latitude and longitude. For example, you may wish to indicate that a photo was taken "indoors" (1) or "outdoors" (2).
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setContext(
        String photoId,
        int context
    ) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_CONTEXT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("context", "" + context));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
        // This method has no specific response - It returns an empty sucess response 
        // if it completes without error.
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
