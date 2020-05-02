
package com.flickr4java.flickr.favorites;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Interface for working with Flickr favorites.
 * 
 * @author Anthony Eden
 * @version $Id: FavoritesInterface.java,v 1.17 2009/07/11 20:30:27 x-mago Exp $
 */
public class FavoritesInterface {

    private static final Logger logger = LoggerFactory.getLogger(FavoritesInterface.class);

    public static final String METHOD_ADD = "flickr.favorites.add";

    public static final String METHOD_GET_LIST = "flickr.favorites.getList";

    public static final String METHOD_GET_PUBLIC_LIST = "flickr.favorites.getPublicList";

    public static final String METHOD_REMOVE = "flickr.favorites.remove";

    public static final String METHOD_GET_CONTEXT = "flickr.favorites.getContext";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public FavoritesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Add a photo to the user's favorites.
     * 
     * @param photoId
     *            The photo ID
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public void add(String photoId) throws  FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD);
        parameters.put("photo_id", photoId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Get the collection of favorites for the calling user or the specified user ID.
     * 
     * @param userId
     *            The optional user ID. Null value will be ignored.
     * @param perPage
     *            The optional per page value. Values {@code <= 0} will be ignored.
     * @param page
     *            The page to view. Values {@code <= 0} will be ignored.
     * @param extras
     *            a Set Strings representing extra parameters to send
     * @return The Collection of Photo objects
     * @see com.flickr4java.flickr.photos.Extras
     */
    public PhotoList<Photo> getList(String userId, int perPage, int page, Set<String> extras) throws  FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        if (userId != null) {
            parameters.put("user_id", userId);
        }
        if (extras != null) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));
        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Get the specified user IDs public contacts.
     * 
     * This method does not require authentication.
     * 
     * @param userId
     *            The user ID
     * @param perPage
     *            The optional per page value. Values {@code <= 0} will be ignored.
     * @param page
     *            The optional page to view. Values {@code <= 0} will be ignored
     * @param extras
     *            A Set of extra parameters to send
     * @return A Collection of Photo objects
     * @throws FlickrException if there was a problem connecting to Flickr
     * @see com.flickr4java.flickr.photos.Extras
     */
    public PhotoList<Photo> getPublicList(String userId, int perPage, int page, Set<String> extras) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PUBLIC_LIST);

        parameters.put("user_id", userId);

        if (extras != null) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));
        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Remove the specified photo from the user's favorites.
     * 
     * @param photoId
     *            The photo id
     */
    public void remove(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REMOVE);

        parameters.put("photo_id", photoId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Returns next and previous favorites for a photo in a user's favorites
     * 
     * @param photoId
     *            The photo id
     * @param userId
     *            The user's ID
     * @see <a href="http://www.flickr.com/services/api/flickr.favorites.getContext.html">flickr.favorites.getContext</a>
     */
    public PhotoContext getContext(String photoId, String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTEXT);

        parameters.put("photo_id", photoId);
        parameters.put("user_id", userId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Collection<Element> payload = response.getPayloadCollection();
        PhotoContext photoContext = new PhotoContext();
        for (Element element : payload) {
            String elementName = element.getTagName();
            if (elementName.equals("prevphoto")) {
                Photo photo = new Photo();
                photo.setId(element.getAttribute("id"));
                photoContext.setPreviousPhoto(photo);
            } else if (elementName.equals("nextphoto")) {
                Photo photo = new Photo();
                photo.setId(element.getAttribute("id"));
                photoContext.setNextPhoto(photo);
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("unsupported element name: " + elementName);
                }
            }
        }
        return photoContext;
    }

}
