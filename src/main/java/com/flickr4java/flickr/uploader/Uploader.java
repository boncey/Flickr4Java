/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.uploader;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.FlickrRuntimeException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.util.StringUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Upload a photo.
 * <p>
 * 
 * Setting {@link com.flickr4java.flickr.uploader.UploadMetaData#setAsync(boolean)} you can switch between synchronous and asynchronous uploads.
 * <p>
 * 
 * Synchronous uploads return the photoId, whilst asynchronous uploads return a ticketId.
 * <p>
 * 
 * TicketId's can be tracked with {@link com.flickr4java.flickr.photos.upload.UploadInterface#checkTickets(Set)} for completion.
 * 
 * @author Anthony Eden
 * @version $Id: Uploader.java,v 1.12 2009/12/15 20:57:49 x-mago Exp $
 */
public class Uploader implements IUploader {
    /**
     * 
     */
    private static final String SERVICES_REPLACE_PATH = "/services/replace/";
    /**
     * 
     */
    private static final String SERVICES_UPLOAD_PATH = "/services/upload/";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transport;

    /**
     * Construct an Uploader.
     *
     * @param apiKey
     *            The API key
     */
    public Uploader(String apiKey, String sharedSecret) {
        this(apiKey, sharedSecret, new REST(Transport.UPLOAD_API_HOST));
        this.transport.setResponseClass(UploaderResponse.class);
    }

    /**
     * Construct an Uploader.
     *
     * @param apiKey
     *            The API key
     * @param transport
     */
    public Uploader(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Upload a photo from a byte-array.
     * 
     * @param data
     *            The photo data as a byte array
     * @param metaData
     *            The meta data
     * @return photoId or ticketId
     * @throws FlickrException
     */
    @Override
    public String upload(byte[] data, UploadMetaData metaData) throws FlickrException {
        Map<String, Object> parameters = setUploadParameters(metaData);
        parameters.put("photo", data);

        UploaderResponse response = postPhoto(parameters, SERVICES_UPLOAD_PATH);

        return getResponseString(metaData.isAsync(), response);
    }

    /**
     * Upload a photo from a File.
     * 
     * @param file
     *            the photo file
     * @param metaData
     *            The meta data
     * @return photoId or ticketId
     * @throws FlickrException
     */
    @Override
    public String upload(File file, UploadMetaData metaData) throws FlickrException {
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            return upload(in, metaData);
        } catch (IOException e) {
            throw new FlickrRuntimeException(e);
        } finally {
            IOUtilities.close(in);
        }
    }

    /**
     * Upload a photo from an InputStream.
     * 
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws FlickrException
     */
    @Override
    public String upload(InputStream in, UploadMetaData metaData) throws FlickrException {
        Map<String, Object> parameters = setUploadParameters(metaData);
        parameters.put("photo", in);

        UploaderResponse response = postPhoto(parameters, SERVICES_UPLOAD_PATH);

        return getResponseString(metaData.isAsync(), response);
    }

    /**
     * Replace a photo from an InputStream.
     * 
     * @param in
     * @return photoId or ticketId
     * @throws FlickrException
     */
    @Override
    public String replace(InputStream in, String flickrId, boolean async) throws FlickrException {
        Map<String, Object> parameters = setReplaceParameters(flickrId, async);
        parameters.put("photo", in);

        UploaderResponse response = postPhoto(parameters, SERVICES_REPLACE_PATH);

        return getResponseString(async, response);
    }

    /**
     * Replace a photo from an InputStream.
     * 
     * @param data
     * @param flickrId
     * @param async
     * @return photoId or ticketId
     * @throws FlickrException
     */
    @Override
    public String replace(byte[] data, String flickrId, boolean async) throws FlickrException {
        Map<String, Object> parameters = setReplaceParameters(flickrId, async);

        parameters.put("photo", data);

        UploaderResponse response = postPhoto(parameters, SERVICES_REPLACE_PATH);

        return getResponseString(async, response);
    }

    /**
     * Replace a photo from a File.
     * 
     * @param file
     * @param flickrId
     * @param async
     * @return photoId or ticketId
     * @throws FlickrException
     */
    @Override
    public String replace(File file, String flickrId, boolean async) throws FlickrException {
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            return replace(in, flickrId, async);
        } catch (FileNotFoundException e) {
            throw new FlickrRuntimeException(e);
        } finally {
            IOUtilities.close(in);
        }
    }

    /**
     * Call the post multipart end point.
     * 
     * @param parameters
     * @param path
     * @return
     * @throws FlickrException
     */
    private UploaderResponse postPhoto(Map<String, Object> parameters, String path) throws FlickrException {
        UploaderResponse response = (UploaderResponse) transport.post(path, parameters, apiKey, sharedSecret, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return response;
    }

    /**
     * Get the photo or ticket id from the response.
     * 
     * @param async
     * @param response
     * @return
     */
    private String getResponseString(boolean async, UploaderResponse response) {
        String id = "";
        if (async) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * 
     * @param metaData
     * @return
     */
    private Map<String, Object> setUploadParameters(UploadMetaData metaData) {
        Map<String, Object> parameters = new TreeMap<String, Object>();

        String filename = metaData.getFilename();
        if(filename == null || filename.equals(""))
        	filename = "image.jpg";  // Will NOT work for videos, filename must be passed.
        parameters.put("filename", filename);

        String fileMimeType = metaData.getFilemimetype();
        if(fileMimeType == null || fileMimeType.equals(""))
        	fileMimeType = "image/jpeg";
        
        parameters.put("filemimetype", fileMimeType);
      
        String title = metaData.getTitle();
        if (title != null) {
            parameters.put("title", title);
        }

        String description = metaData.getDescription();
        if (description != null) {
            parameters.put("description", description);
        }

        Collection<String> tags = metaData.getTags();
        if (tags != null) {
            parameters.put("tags", StringUtilities.join(tags, " "));
        }

        if (metaData.isHidden() != null) {
            parameters.put("hidden", metaData.isHidden().booleanValue() ? "1" : "0");
        }

        if (metaData.getSafetyLevel() != null) {
            parameters.put("safety_level", metaData.getSafetyLevel());
        }

        if (metaData.getContentType() != null) {
            parameters.put("content_type", metaData.getContentType());
        }

        parameters.put("is_public", metaData.isPublicFlag() ? "1" : "0");
        parameters.put("is_family", metaData.isFamilyFlag() ? "1" : "0");
        parameters.put("is_friend", metaData.isFriendFlag() ? "1" : "0");
        parameters.put("async", metaData.isAsync() ? "1" : "0");

        return parameters;
    }

    /**
     * 
     * @param flickrId
     * @param async
     * @return
     */
    private Map<String, Object> setReplaceParameters(String flickrId, boolean async) {
        Map<String, Object> parameters = new TreeMap<String, Object>();

        parameters.put("async", async ? "1" : "0");
        parameters.put("photo_id", flickrId);

        return parameters;
    }

    /**
     * Return the {@link REST} impl used by this instance so that properties can be set on it, eg {@link REST#setConnectTimeoutMs(Integer)}. TODO: should return
     * a wrapper that only allows "safe" properties to be set.
     * 
     * @return The {@link REST} transport used by this instance
     */
    public REST getTransport() {
        return (REST) transport;
    }
}
