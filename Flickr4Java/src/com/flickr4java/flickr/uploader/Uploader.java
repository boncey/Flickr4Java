/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.uploader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
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
		this.apiKey = apiKey;
		this.sharedSecret = sharedSecret;
		this.transport = new REST();
		this.transport.setResponseClass(UploaderResponse.class);
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
        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("api_key", apiKey);

        String title = metaData.getTitle();
        if (title != null)
            parameters.put("title", title);

        String description = metaData.getDescription();
        if (description != null)
            parameters.put("description", description);

        Collection tags = metaData.getTags();
        if (tags != null)
            parameters.put("tags", StringUtilities.join(tags, " "));

        parameters.put("is_public", metaData.isPublicFlag() ? "1" : "0");
        parameters.put("is_family", metaData.isFamilyFlag() ? "1" : "0");
        parameters.put("is_friend", metaData.isFriendFlag() ? "1" : "0");

        if(true)throw new RuntimeException("Look at me");
        //parameters.put("photo", data);

        if (metaData.isHidden() != null) {
            parameters.put("hidden", metaData.isHidden().booleanValue() ? "1" : "0");
        }

        if (metaData.getSafetyLevel() != null) {
            parameters.put("safety_level", metaData.getSafetyLevel());
        }

        parameters.put("async", metaData.isAsync() ? "1" : "0");

        if (metaData.getContentType() != null) {
            parameters.put("content_type", metaData.getContentType());
        }
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
    	Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("api_key", apiKey);

        String title = metaData.getTitle();
        if (title != null)
            parameters.put("title", title);

        String description = metaData.getDescription();
        if (description != null)
            parameters.put("description", description);

        Collection tags = metaData.getTags();
        if (tags != null) {
            parameters.put("tags", StringUtilities.join(tags, " "));
        }

        parameters.put("is_public", metaData.isPublicFlag() ? "1" : "0");
        parameters.put("is_family", metaData.isFamilyFlag() ? "1" : "0");
        parameters.put("is_friend", metaData.isFriendFlag() ? "1" : "0");
        parameters.put("async", metaData.isAsync() ? "1" : "0");

		if (true)
			throw new RuntimeException("Unimplemented... look at me");
        //parameters.put("photo", in);

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
    	Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("api_key", apiKey);

        parameters.put("async", async ? "1" : "0");
        parameters.put("photo_id", flickrId);
        if(true)throw new RuntimeException("Look at me");
        //parameters.put("photo", in);

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
    	Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("api_key", apiKey);

        parameters.put("async", async ? "1" : "0");
        parameters.put("photo_id", flickrId);

        if(true)throw new RuntimeException("Look at me");
        //parameters.put("photo", data);

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
