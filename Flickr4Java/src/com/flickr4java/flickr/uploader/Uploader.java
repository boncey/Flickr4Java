/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.uploader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.util.StringUtilities;

/**
 * Upload a photo.<p>
 *
 * Setting {@link com.flickr4java.flickr.test.uploader.UploadMetaData#setAsync(boolean)}
 * you can switch between synchronous and asynchronous uploads.<p>
 *
 * Synchronous uploads return the photoId, whilst asynchronous uploads
 * return a ticketId.<p>
 *
 * TicketId's can be tracked with
 * {@link com.flickr4java.flickr.test.photos.upload.UploadInterface#checkTickets(Set)}
 * for completion.
 *
 * @author Anthony Eden
 * @version $Id: Uploader.java,v 1.12 2009/12/15 20:57:49 x-mago Exp $
 */
public class Uploader {
    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    /**
     * Construct an Uploader.
     *
     * @param apiKey The API key
     */
    public Uploader(String apiKey, String sharedSecret) {
        try {
            this.apiKey = apiKey;
            this.sharedSecret = sharedSecret;
            this.transport = new REST();
            this.transport.setResponseClass(UploaderResponse.class);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Upload a photo from a byte-array.
     *
     * @param data The photo data as a byte array
     * @param metaData The meta data
     * @return photoId or ticketId
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     */
    public String upload(byte[] data, UploadMetaData metaData) throws FlickrException, IOException, SAXException {
        List parameters = new ArrayList();

        parameters.add(new Parameter("api_key", apiKey));

        String title = metaData.getTitle();
        if (title != null)
            parameters.add(new Parameter("title", title));

        String description = metaData.getDescription();
        if (description != null)
            parameters.add(new Parameter("description", description));

        Collection tags = metaData.getTags();
        if (tags != null)
            parameters.add(new Parameter("tags", StringUtilities.join(tags, " ")));

        parameters.add(new Parameter("is_public", metaData.isPublicFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_family", metaData.isFamilyFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", metaData.isFriendFlag() ? "1" : "0"));

        parameters.add(new Parameter("photo", data));

        if (metaData.isHidden() != null) {
            parameters.add(new Parameter("hidden", metaData.isHidden().booleanValue() ? "1" : "0"));
        }

        if (metaData.getSafetyLevel() != null) {
            parameters.add(new Parameter("safety_level", metaData.getSafetyLevel()));
        }

        parameters.add(new Parameter("async", metaData.isAsync() ? "1" : "0"));

        if (metaData.getContentType() != null) {
            parameters.add(new Parameter("content_type", metaData.getContentType()));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/upload/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (metaData.isAsync()) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * Upload a photo from an InputStream.
     *
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public String upload(InputStream in, UploadMetaData metaData) throws IOException, FlickrException, SAXException {
        List parameters = new ArrayList();

        parameters.add(new Parameter("api_key", apiKey));

        String title = metaData.getTitle();
        if (title != null)
            parameters.add(new Parameter("title", title));

        String description = metaData.getDescription();
        if (description != null)
            parameters.add(new Parameter("description", description));

        Collection tags = metaData.getTags();
        if (tags != null) {
            parameters.add(new Parameter("tags", StringUtilities.join(tags, " ")));
        }

        parameters.add(new Parameter("is_public", metaData.isPublicFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_family", metaData.isFamilyFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", metaData.isFriendFlag() ? "1" : "0"));
        parameters.add(new Parameter("async", metaData.isAsync() ? "1" : "0"));

        parameters.add(new Parameter("photo", in));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/upload/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (metaData.isAsync()) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * Upload a photo from an InputStream.
     *
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public String replace(InputStream in, String flickrId, boolean async) throws IOException, FlickrException, SAXException {
        List parameters = new ArrayList();

        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("async", async ? "1" : "0"));
        parameters.add(new Parameter("photo_id", flickrId));

        parameters.add(new Parameter("photo", in));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/replace/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (async) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * Upload a photo from an InputStream.
     *
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public String replace(byte[] data, String flickrId, boolean async) throws IOException, FlickrException, SAXException {
        List parameters = new ArrayList();

        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("async", async ? "1" : "0"));
        parameters.add(new Parameter("photo_id", flickrId));

        parameters.add(new Parameter("photo", data));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/replace/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (async) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }
}
