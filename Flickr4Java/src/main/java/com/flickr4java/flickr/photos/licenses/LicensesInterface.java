/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.photos.licenses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;

/**
 * Interface for working with copyright licenses.
 * 
 * @author Anthony Eden
 */
public class LicensesInterface {

    public static final String METHOD_GET_INFO = "flickr.photos.licenses.getInfo";

    public static final String METHOD_SET_LICENSE = "flickr.photos.licenses.setLicense";

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

    public LicensesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Fetches a list of available photo licenses for Flickr.
     * 
     * This method does not require authentication.
     * 
     * @return A collection of License objects
     * @throws FlickrException
     */
    public Collection<License> getInfo() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);
        parameters.put(Flickr.API_KEY, apiKey);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<License> licenses = new ArrayList<License>();
        Element licensesElement = response.getPayload();
        NodeList licenseElements = licensesElement.getElementsByTagName("license");
        for (int i = 0; i < licenseElements.getLength(); i++) {
            Element licenseElement = (Element) licenseElements.item(i);
            License license = new License();
            license.setId(licenseElement.getAttribute("id"));
            license.setName(licenseElement.getAttribute("name"));
            license.setUrl(licenseElement.getAttribute("url"));
            licenses.add(license);
        }
        return licenses;
    }

    /**
     * Sets the license for a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo to update the license for.
     * @param licenseId
     *            The license to apply, or 0 (zero) to remove the current license.
     * @throws FlickrException
     */
    public void setLicense(String photoId, int licenseId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_LICENSE);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("photo_id", photoId);
        parameters.put("license_id", Integer.toString(licenseId));

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty sucess response if it completes without error.

    }

}
