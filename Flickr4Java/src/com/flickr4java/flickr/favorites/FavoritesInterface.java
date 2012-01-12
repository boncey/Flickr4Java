/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.favorites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;

/**
 * Interface for working with Flickr favorites.
 *
 * @author Anthony Eden
 * @version $Id: FavoritesInterface.java,v 1.17 2009/07/11 20:30:27 x-mago Exp $
 */
public class FavoritesInterface {

    public static final String METHOD_ADD = "flickr.favorites.add";
    public static final String METHOD_GET_LIST = "flickr.favorites.getList";
    public static final String METHOD_GET_PUBLIC_LIST = "flickr.favorites.getPublicList";
    public static final String METHOD_REMOVE = "flickr.favorites.remove";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public FavoritesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Add a photo to the user's favorites.
     *
     * @param photoId The photo ID
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void add(String photoId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_ADD));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Get the collection of favorites for the calling user or the specified user ID.
     *
     * @param userId The optional user ID.  Null value will be ignored.
     * @param perPage The optional per page value.  Values <= 0 will be ignored.
     * @param page The page to view.  Values <= 0 will be ignored.
     * @param extras a Set Strings representing extra parameters to send
     * @return The Collection of Photo objects
     * @see com.flickr4java.flickr.photos.Extras
     * @throws IOException
     * @throws SAXException
     */
    public PhotoList getList(String userId, int perPage, int page, Set extras) throws IOException,
            SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        if (userId != null) {
            parameters.add(new Parameter("user_id", userId));
        }
        if (extras != null) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", new Integer(perPage)));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", new Integer(page)));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
     * @param userId The user ID
     * @param perPage The optional per page value.  Values <= 0 will be ignored.
     * @param page The optional page to view.  Values <= 0 will be ignored
     * @param extras A Set of extra parameters to send
     * @return A Collection of Photo objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     * @see com.flickr4java.flickr.photos.Extras
     */
    public PhotoList getPublicList(String userId, int perPage, int page, Set extras)
            throws IOException, SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_PUBLIC_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));

        if (extras != null) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", new Integer(perPage)));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", new Integer(page)));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
     * @param photoId The photo id
     */
    public void remove(String photoId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_REMOVE));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
