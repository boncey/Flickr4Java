
package com.flickr4java.flickr.urls;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.galleries.Gallery;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface for testing Flickr connectivity.
 * 
 * @author Anthony Eden
 */
public class UrlsInterface {

    public static final String METHOD_GET_GROUP = "flickr.urls.getGroup";

    public static final String METHOD_GET_USER_PHOTOS = "flickr.urls.getUserPhotos";

    public static final String METHOD_GET_USER_PROFILE = "flickr.urls.getUserProfile";

    public static final String METHOD_LOOKUP_GROUP = "flickr.urls.lookupGroup";

    public static final String METHOD_LOOKUP_USER = "flickr.urls.lookupUser";

    public static final String METHOD_LOOKUP_GALLERY = "flickr.urls.lookupGallery";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transport;

    /**
     * Construct a UrlsInterface.
     * 
     * @param apiKey
     *            The API key
     * @param transportAPI
     *            The Transport interface
     */
    public UrlsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transportAPI;
    }

    /**
     * Get the group URL for the specified group ID
     * 
     * @param groupId
     *            The group ID
     * @return The group URL
     * @throws FlickrException
     */
    public String getGroup(String groupId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_GROUP);

        parameters.put("group_id", groupId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element payload = response.getPayload();
        return payload.getAttribute("url");
    }

    /**
     * Get the URL for the user's photos.
     * 
     * @param userId
     *            The user ID
     * @return The user photo URL
     * @throws FlickrException
     */
    public String getUserPhotos(String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_USER_PHOTOS);

        parameters.put("user_id", userId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element payload = response.getPayload();
        return payload.getAttribute("url");
    }

    /**
     * Get the URL for the user's profile.
     * 
     * @param userId
     *            The user ID
     * @return The URL
     * @throws FlickrException
     */
    public String getUserProfile(String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_USER_PROFILE);

        parameters.put("user_id", userId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element payload = response.getPayload();
        return payload.getAttribute("url");
    }

    /**
     * Lookup the group for the specified URL.
     * 
     * @param url
     *            The url
     * @return The group
     * @throws FlickrException
     */
    public Group lookupGroup(String url) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_LOOKUP_GROUP);

        parameters.put("url", url);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Group group = new Group();
        Element payload = response.getPayload();
        Element groupnameElement = (Element) payload.getElementsByTagName("groupname").item(0);
        group.setId(payload.getAttribute("id"));
        group.setName(((Text) groupnameElement.getFirstChild()).getData());
        return group;
    }

    /**
     * Lookup the username for the specified User URL.
     * 
     * @param url
     *            The user profile URL
     * @return The username
     * @throws FlickrException
     */
    public String lookupUser(String url) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_LOOKUP_USER);

        parameters.put("url", url);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element payload = response.getPayload();
        Element groupnameElement = (Element) payload.getElementsByTagName("username").item(0);
        return ((Text) groupnameElement.getFirstChild()).getData();
    }

    /**
     * Lookup the Gallery for the specified ID.
     * 
     * @param galleryId
     *            The user profile URL
     * @return The Gallery
     * @throws FlickrException
     */
    public Gallery lookupGallery(String galleryId) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_LOOKUP_GALLERY);
        parameters.put("url", galleryId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element galleryElement = response.getPayload();
        Gallery gallery = new Gallery();
        gallery.setId(galleryElement.getAttribute("id"));
        gallery.setUrl(galleryElement.getAttribute("url"));

        User owner = new User();
        owner.setId(galleryElement.getAttribute("owner"));
        gallery.setOwner(owner);
        gallery.setCreateDate(galleryElement.getAttribute("date_create"));
        gallery.setUpdateDate(galleryElement.getAttribute("date_update"));
        gallery.setPrimaryPhotoId(galleryElement.getAttribute("primary_photo_id"));
        gallery.setPrimaryPhotoServer(galleryElement.getAttribute("primary_photo_server"));
        gallery.setVideoCount(galleryElement.getAttribute("count_videos"));
        gallery.setPhotoCount(galleryElement.getAttribute("count_photos"));
        gallery.setPrimaryPhotoFarm(galleryElement.getAttribute("farm"));
        gallery.setPrimaryPhotoSecret(galleryElement.getAttribute("secret"));

        gallery.setTitle(XMLUtilities.getChildValue(galleryElement, "title"));
        gallery.setDesc(XMLUtilities.getChildValue(galleryElement, "description"));
        return gallery;
    }

}
