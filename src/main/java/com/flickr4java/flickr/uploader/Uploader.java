/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.uploader;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

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

    private static final String SERVICES_REPLACE_PATH = "/services/replace/";

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
        Payload payload = new Payload(data);
        return sendUploadRequest(metaData, payload);
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
        Payload payload = new Payload(file);
        return sendUploadRequest(metaData, payload);
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
        Payload payload = new Payload(in);
        return sendUploadRequest(metaData, payload);
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
        Payload payload = new Payload(in, flickrId);
        return sendReplaceRequest(async, payload);
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
        Payload payload = new Payload(data, flickrId);
        return sendReplaceRequest(async, payload);
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
        Payload payload = new Payload(file, flickrId);
        return sendReplaceRequest(async, payload);
    }

    private String sendUploadRequest(UploadMetaData metaData, Payload payload) throws FlickrException {
        UploaderResponse response = (UploaderResponse) transport.postMultiPart(SERVICES_UPLOAD_PATH, metaData, payload, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        return getResponseString(metaData.isAsync(), response);
    }

    private String sendReplaceRequest(boolean async, Payload payload) throws FlickrException {
        UploaderResponse response = (UploaderResponse) transport.postMultiPart(SERVICES_REPLACE_PATH, UploadMetaData.replace(async, payload.getPhotoId()), payload, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        return getResponseString(async, response);
    }

    /**
     * Get the photo or ticket id from the response.
     * 
     * @param async
     * @param response
     * @return
     */
    private String getResponseString(boolean async, UploaderResponse response) {
        return async ? response.getTicketId() : response.getPhotoId();
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
