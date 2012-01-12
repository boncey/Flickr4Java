/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.photos.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;

import org.xml.sax.SAXException;

/**
 * @author Anthony Eden
 * @version $Id: TransformInterface.java,v 1.6 2008/01/28 23:01:44 x-mago Exp $
 */
public class TransformInterface {

    public static final String METHOD_ROTATE = "flickr.photos.transform.rotate";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public TransformInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Rotate the specified photo.  The only allowed values for degrees are 90, 180 and 270.
     *
     * @param photoId The photo ID
     * @param degrees The degrees to rotate (90, 170 or 270)
     */
    public void rotate(String photoId, int degrees)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_ROTATE));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("degrees", String.valueOf(degrees)));
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
