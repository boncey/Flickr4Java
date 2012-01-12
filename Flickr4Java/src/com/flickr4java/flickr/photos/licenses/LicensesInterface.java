/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.photos.licenses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Interface for working with copyright licenses.
 *
 * @author Anthony Eden
 */
public class LicensesInterface {

    public static final String METHOD_GET_INFO    = "flickr.photos.licenses.getInfo";
    public static final String METHOD_SET_LICENSE = "flickr.photos.licenses.setLicense";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public LicensesInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
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
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Collection getInfo() throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List licenses = new ArrayList();
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
     * @param photoId The photo to update the license for.
     * @param licenseId The license to apply, or 0 (zero) to remove the current license.
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setLicense(String photoId, int licenseId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_LICENSE));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("license_id", licenseId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty sucess response if it completes without error.

    }

}
