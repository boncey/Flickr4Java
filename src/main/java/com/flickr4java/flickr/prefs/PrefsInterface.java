/*
 * Copyright (c) 2007 Martin Goebel
 */
package com.flickr4java.flickr.prefs;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Requesting preferences for the current authenticated user.
 * 
 * @author Martin Goebel
 * @version $Id: PrefsInterface.java,v 1.6 2008/06/28 22:30:04 x-mago Exp $
 */
public class PrefsInterface {
    public static final String METHOD_GET_CONTENT_TYPE = "flickr.prefs.getContentType";

    public static final String METHOD_GET_HIDDEN = "flickr.prefs.getHidden";

    public static final String METHOD_GET_SAFETY_LEVEL = "flickr.prefs.getSafetyLevel";

    public static final String METHOD_GET_PRIVACY = "flickr.prefs.getPrivacy";

    public static final String METHOD_GET_GEO_PERMS = "flickr.prefs.getGeoPerms";

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

    /**
     * Construct a PrefsInterface.
     * 
     * @param apiKey
     *            The API key
     * @param transportAPI
     *            The Transport interface
     */
    public PrefsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Returns the default content type preference for the user.
     * 
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_OTHER
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_PHOTO
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_SCREENSHOT
     * @return The content-type
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public String getContentType() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTENT_TYPE);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element personElement = response.getPayload();
        return personElement.getAttribute("content_type");
    }

    /**
     * Returns the default privacy level for geographic information attached to the user's photos.
     * 
     * @return privacy-level
     * @throws FlickrException if there was a problem connecting to Flickr
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_NO_FILTER
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_PUBLIC
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_PRIVATE
     */
    public int getGeoPerms() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_GEO_PERMS);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        int perm = -1;
        Element personElement = response.getPayload();
        String geoPerms = personElement.getAttribute("geoperms");
        try {
            perm = Integer.parseInt(geoPerms);
        } catch (NumberFormatException e) {
            throw new FlickrException("0", "Unable to parse geoPermission");
        }
        return perm;
    }

    /**
     * Returns the default hidden preference for the user.
     * 
     * @return boolean hidden or not
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public boolean getHidden() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_HIDDEN);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element personElement = response.getPayload();
        return personElement.getAttribute("hidden").equals("1") ? true : false;
    }

    /**
     * Returns the default safety level preference for the user.
     * 
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_MODERATE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_RESTRICTED
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_SAFE
     * @return The current users safety-level
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public String getSafetyLevel() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_SAFETY_LEVEL);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element personElement = response.getPayload();
        return personElement.getAttribute("safety_level");
    }

    /**
     * Returns the default privacy level preference for the user.
     * 
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_NO_FILTER
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_PUBLIC
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FAMILY
     * @see com.flickr4java.flickr.Flickr#PRIVACY_LEVEL_FRIENDS
     * @throws FlickrException if there was a problem connecting to Flickr
     * @return privacyLevel
     */
    public int getPrivacy() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PRIVACY);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element personElement = response.getPayload();
        return Integer.parseInt(personElement.getAttribute("privacy"));
    }
}
