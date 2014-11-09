/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photosets;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for working with photosets.
 * 
 * @author Anthony Eden
 * @version $Id: PhotosetsInterface.java,v 1.27 2009/11/08 21:58:00 x-mago Exp $
 */
public class PhotosetsInterface {

    private static Logger _log = Logger.getLogger(PhotosetsInterface.class);

    public static final String METHOD_ADD_PHOTO = "flickr.photosets.addPhoto";

    public static final String METHOD_CREATE = "flickr.photosets.create";

    public static final String METHOD_DELETE = "flickr.photosets.delete";

    public static final String METHOD_EDIT_META = "flickr.photosets.editMeta";

    public static final String METHOD_EDIT_PHOTOS = "flickr.photosets.editPhotos";

    public static final String METHOD_GET_CONTEXT = "flickr.photosets.getContext";

    public static final String METHOD_GET_INFO = "flickr.photosets.getInfo";

    public static final String METHOD_GET_LIST = "flickr.photosets.getList";

    public static final String METHOD_GET_PHOTOS = "flickr.photosets.getPhotos";

    public static final String METHOD_ORDER_SETS = "flickr.photosets.orderSets";

    public static final String METHOD_REMOVE_PHOTO = "flickr.photosets.removePhoto";

    public static final String METHOD_REMOVE_PHOTOS = "flickr.photosets.removePhotos";

    public static final String METHOD_REORDER_PHOTOS = "flickr.photosets.reorderPhotos";

    public static final String METHOD_SET_PRIMARY_PHOTO = "flickr.photosets.setPrimaryPhoto";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public PhotosetsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Add a photo to the end of the photoset.
     * <p/>
     * Note: requires authentication with the new authentication API with 'write' permission.
     * 
     * @param photosetId
     *            The photoset ID
     * @param photoId
     *            The photo ID
     */
    public void addPhoto(String photosetId, String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD_PHOTO);

        parameters.put("photoset_id", photosetId);
        parameters.put("photo_id", photoId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Create a new photoset.
     * 
     * @param title
     *            The photoset title
     * @param description
     *            The photoset description
     * @param primaryPhotoId
     *            The primary photo id
     * @return The new Photset
     * @throws FlickrException
     */
    public Photoset create(String title, String description, String primaryPhotoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_CREATE);

        parameters.put("title", title);
        parameters.put("description", description);
        parameters.put("primary_photo_id", primaryPhotoId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosetElement = response.getPayload();
        Photoset photoset = new Photoset();
        photoset.setId(photosetElement.getAttribute("id"));
        photoset.setUrl(photosetElement.getAttribute("url"));
        return photoset;
    }

    /**
     * Delete the specified photoset.
     * 
     * @param photosetId
     *            The photoset ID
     * @throws FlickrException
     */
    public void delete(String photosetId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_DELETE);

        parameters.put("photoset_id", photosetId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Modify the meta-data for a photoset.
     * 
     * @param photosetId
     *            The photoset ID
     * @param title
     *            A new title
     * @param description
     *            A new description (can be null)
     * @throws FlickrException
     */
    public void editMeta(String photosetId, String title, String description) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_META);

        parameters.put("photoset_id", photosetId);
        parameters.put("title", title);
        if (description != null) {
            parameters.put("description", description);
        }

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Edit which photos are in the photoset.
     * 
     * @param photosetId
     *            The photoset ID
     * @param primaryPhotoId
     *            The primary photo Id
     * @param photoIds
     *            The photo IDs for the photos in the set
     * @throws FlickrException
     */
    public void editPhotos(String photosetId, String primaryPhotoId, String[] photoIds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_PHOTOS);

        parameters.put("photoset_id", photosetId);
        parameters.put("primary_photo_id", primaryPhotoId);
        parameters.put("photo_ids", StringUtilities.join(photoIds, ","));

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Get a photo's context in the specified photo set.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo ID
     * @param photosetId
     *            The photoset ID
     * @return The PhotoContext
     * @throws FlickrException
     */
    public PhotoContext getContext(String photoId, String photosetId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTEXT);

        parameters.put("photo_id", photoId);
        parameters.put("photoset_id", photosetId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
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
            } else if (elementName.equals("count")) {
                // TODO: process this information
            } else {
                _log.warn("unsupported element name: " + elementName);
            }
        }
        return photoContext;
    }

    /**
     * Get the information for a specified photoset.
     * 
     * This method does not require authentication.
     * 
     * @param photosetId
     *            The photoset ID
     * @return The Photoset
     * @throws FlickrException
     */
    public Photoset getInfo(String photosetId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);

        parameters.put("photoset_id", photosetId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosetElement = response.getPayload();
        Photoset photoset = new Photoset();
        photoset.setId(photosetElement.getAttribute("id"));

        User owner = new User();
        owner.setId(photosetElement.getAttribute("owner"));
        photoset.setOwner(owner);

        Photo primaryPhoto = new Photo();
        primaryPhoto.setId(photosetElement.getAttribute("primary"));
        primaryPhoto.setSecret(photosetElement.getAttribute("secret")); // TODO verify that this is the secret for the photo
        primaryPhoto.setServer(photosetElement.getAttribute("server")); // TODO verify that this is the server for the photo
        primaryPhoto.setFarm(photosetElement.getAttribute("farm"));
        photoset.setPrimaryPhoto(primaryPhoto);

        // TODO remove secret/server/farm from photoset?
        // It's rather related to the primaryPhoto, then to the photoset itself.
        photoset.setSecret(photosetElement.getAttribute("secret"));
        photoset.setServer(photosetElement.getAttribute("server"));
        photoset.setFarm(photosetElement.getAttribute("farm"));
        photoset.setPhotoCount(photosetElement.getAttribute("count_photos"));
        photoset.setVideoCount(Integer.parseInt(photosetElement.getAttribute("count_videos")));
        photoset.setViewCount(Integer.parseInt(photosetElement.getAttribute("count_views")));
        photoset.setCommentCount(Integer.parseInt(photosetElement.getAttribute("count_comments")));
        photoset.setDateCreate(photosetElement.getAttribute("date_create"));
        photoset.setDateUpdate(photosetElement.getAttribute("date_update"));

        photoset.setIsCanComment("1".equals(photosetElement.getAttribute("can_comment")));

        photoset.setTitle(XMLUtilities.getChildValue(photosetElement, "title"));
        photoset.setDescription(XMLUtilities.getChildValue(photosetElement, "description"));
        photoset.setPrimaryPhoto(primaryPhoto);

        return photoset;
    }

    /**
     * Get a list of all photosets for the specified user.
     * 
     * This method does not require authentication. But to get a Photoset into the list, that contains just private photos, the call needs to be authenticated.
     * 
     * @param userId
     *            The User id
     * @return The Photosets collection
     * @throws FlickrException
     */
    public Photosets getList(String userId) throws FlickrException {
        return getList(userId, 0, 0, null);
    }

    /**
     * Get a list of all photosets for the specified user.
     * 
     * This method does not require authentication. But to get a Photoset into the list, that contains just private photos, the call needs to be authenticated.
     * 
     * @param userId
     *            The User id
     * @param primaryPhotoExtras
     *            A comma-delimited list of extra information to fetch for the primary photo
     * @return The Photosets collection
     * @throws FlickrException
     */
    public Photosets getList(String userId, String primaryPhotoExtras) throws FlickrException {
        return getList(userId, 0, 0, primaryPhotoExtras);
    }

    /**
     * Get a list of all photosets for the specified user.
     * 
     * This method does not require authentication. But to get a Photoset into the list, that contains just private photos, the call needs to be authenticated.
     * 
     * @param userId
     *            The User id
     * @param perPage
     *            The number of photosets per page
     * @param page
     *            The page offset
     * @param primaryPhotoExtras
     *            A comma-delimited list of extra information to fetch for the primary photo
     * @return The Photosets collection
     * @throws FlickrException
     */
    public Photosets getList(String userId, int perPage, int page, String primaryPhotoExtras) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        if (userId != null) {
            parameters.put("user_id", userId);
        }

        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }

        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        if (primaryPhotoExtras != null) {
            parameters.put("primary_photo_extras", primaryPhotoExtras);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Photosets photosetsObject = new Photosets();
        Element photosetsElement = response.getPayload();
        photosetsObject.setCanCreate(XMLUtilities.getBooleanAttribute(photosetsElement, "cancreate"));
        photosetsObject.setPage(XMLUtilities.getIntAttribute(photosetsElement, "page"));
        photosetsObject.setPages(XMLUtilities.getIntAttribute(photosetsElement, "pages"));
        photosetsObject.setPerPage(XMLUtilities.getIntAttribute(photosetsElement, "perpage"));
        photosetsObject.setTotal(XMLUtilities.getIntAttribute(photosetsElement, "total"));
        List<Photoset> photosets = new ArrayList<Photoset>();
        NodeList photosetElements = photosetsElement.getElementsByTagName("photoset");
        for (int i = 0; i < photosetElements.getLength(); i++) {
            Element photosetElement = (Element) photosetElements.item(i);
            Photoset photoset = new Photoset();
            photoset.setId(photosetElement.getAttribute("id"));

            User owner = new User();
            owner.setId(photosetElement.getAttribute("owner"));
            photoset.setOwner(owner);

            Element primaryPhotoExtrasEl = XMLUtilities.getChild(photosetElement, "primary_photo_extras");
            Photo primaryPhoto;
            if (primaryPhotoExtrasEl != null) {
                primaryPhoto = PhotoUtils.createPhoto(primaryPhotoExtrasEl);
            } else {
                primaryPhoto = new Photo();
            }
            primaryPhoto.setId(photosetElement.getAttribute("primary"));
            primaryPhoto.setSecret(photosetElement.getAttribute("secret")); // TODO verify that this is the secret for the photo
            primaryPhoto.setServer(photosetElement.getAttribute("server")); // TODO verify that this is the server for the photo
            primaryPhoto.setFarm(photosetElement.getAttribute("farm"));
            photoset.setPrimaryPhoto(primaryPhoto);

            photoset.setSecret(photosetElement.getAttribute("secret"));
            photoset.setServer(photosetElement.getAttribute("server"));
            photoset.setFarm(photosetElement.getAttribute("farm"));
            photoset.setPhotoCount(photosetElement.getAttribute("photos"));
            photoset.setVideoCount(Integer.parseInt(photosetElement.getAttribute("videos")));
            photoset.setViewCount(Integer.parseInt(photosetElement.getAttribute("count_views")));
            photoset.setCommentCount(Integer.parseInt(photosetElement.getAttribute("count_comments")));
            photoset.setDateCreate(photosetElement.getAttribute("date_create"));
            photoset.setDateUpdate(photosetElement.getAttribute("date_update"));

            photoset.setIsCanComment("1".equals(photosetElement.getAttribute("can_comment")));
            photoset.setIsNeedsInterstitial("1".equals(photosetElement.getAttribute("needs_interstitial")));
            photoset.setIsVisible("1".equals(photosetElement.getAttribute("visibility_can_see_set")));
            photoset.setDescription(XMLUtilities.getChildValue(photosetElement, "description"));

            photoset.setTitle(XMLUtilities.getChildValue(photosetElement, "title"));
            photoset.setDescription(XMLUtilities.getChildValue(photosetElement, "description"));

            photosets.add(photoset);
        }

        photosetsObject.setPhotosets(photosets);

        return photosetsObject;
    }

    /**
     * Convenience method.
     * 
     * Get the total number of sets belonging to a user
     * 
     * @param userId
     *            The User id
     * @return int The number of photosets
     * @throws FlickrException
     */
    public int getPhotosetCount(String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        if (userId != null) {
            parameters.put("user_id", userId);
        }

        // Request the bare minimum
        parameters.put("per_page", String.valueOf(1));
        parameters.put("page", String.valueOf(1));

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosetsElement = response.getPayload();

        return Integer.valueOf(photosetsElement.getAttribute("total"));
    }

    /**
     * Get a collection of Photo objects for the specified Photoset.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_NO_FILTER
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_PUBLIC
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @param photosetId
     *            The photoset ID
     * @param extras
     *            Set of extra-fields
     * @param privacy_filter
     *            filter value for authenticated calls
     * @param perPage
     *            The number of photos per page
     * @param page
     *            The page offset
     * @return PhotoList The Collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getPhotos(String photosetId, Set<String> extras, int privacy_filter, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PHOTOS);

        parameters.put("photoset_id", photosetId);

        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }

        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        if (privacy_filter > 0) {
            parameters.put("privacy_filter", "" + privacy_filter);
        }

        if (extras != null && !extras.isEmpty()) {
            parameters.put(Extras.KEY_EXTRAS, StringUtilities.join(extras, ","));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element photoset = response.getPayload();
        NodeList photoElements = photoset.getElementsByTagName("photo");
        photos.setPage(photoset.getAttribute("page"));
        photos.setPages(photoset.getAttribute("pages"));
        photos.setPerPage(photoset.getAttribute("per_page"));
        photos.setTotal(photoset.getAttribute("total"));

        for (int i = 0; i < photoElements.getLength(); i++) {
            Element photoElement = (Element) photoElements.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement, photoset));
        }

        return photos;
    }

    /**
     * Convenience method.
     * 
     * Calls getPhotos() with Extras.MIN_EXTRAS and Flickr.PRIVACY_LEVEL_NO_FILTER.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_NO_FILTER
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_PUBLIC
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @param photosetId
     *            The photoset ID
     * @param perPage
     *            The number of photos per page
     * @param page
     *            The page offset
     * @return PhotoList The Collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getPhotos(String photosetId, int perPage, int page) throws FlickrException {
        return getPhotos(photosetId, Extras.MIN_EXTRAS, Flickr.PRIVACY_LEVEL_NO_FILTER, perPage, page);
    }

    /**
     * Set the order in which sets are returned for the user.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photosetIds
     *            An array of Ids
     * @throws FlickrException
     */
    public void orderSets(String[] photosetIds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ORDER_SETS);
        ;

        parameters.put("photoset_ids", StringUtilities.join(photosetIds, ","));

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Remove a photo from the set.
     * 
     * @param photosetId
     *            The photoset ID
     * @param photoId
     *            The photo ID
     * @throws FlickrException
     */
    public void removePhoto(String photosetId, String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REMOVE_PHOTO);

        parameters.put("photoset_id", photosetId);
        parameters.put("photo_id", photoId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Remove a photo from the set.
     * 
     * @param photosetId
     *            The photoset ID
     * @param photoIds
     *            The ID's of the photos, in CVS format
     * @throws FlickrException
     */
    public void removePhotos(String photosetId, String photoIds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REMOVE_PHOTOS);

        parameters.put("photoset_id", photosetId);
        parameters.put("photo_ids", photoIds);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Remove a photo from the set.
     * 
     * @param photosetId
     *            The photoset ID
     * @param photoIds
     *            The ID's of the photos, in CSV format, in the order they need to be in.
     * @throws FlickrException
     */
    public void reorderPhotos(String photosetId, String photoIds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REORDER_PHOTOS);

        parameters.put("photoset_id", photosetId);
        parameters.put("photo_ids", photoIds);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Remove a photo from the set.
     * 
     * @param photosetId
     *            The photoset ID
     * @param photoId
     *            The photo ID that is being added
     * @throws FlickrException
     */
    public void setPrimaryPhoto(String photosetId, String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_PRIMARY_PHOTO);

        parameters.put("photoset_id", photosetId);
        parameters.put("photo_id", photoId);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
