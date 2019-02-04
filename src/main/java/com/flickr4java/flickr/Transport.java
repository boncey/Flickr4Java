/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import com.flickr4java.flickr.uploader.Payload;
import com.flickr4java.flickr.uploader.UploadMetaData;

import java.util.Map;

/**
 * The abstract Transport class provides a common interface for transporting requests to the Flickr servers.
 * Flickr offers several transport methods including REST, SOAP and XML-RPC.
 * Flickr4Java currently implements the REST transport.
 * 
 * @author Matt Ray
 * @author Anthony Eden
 */
public abstract class Transport {

    public static final String REST = "REST";

    protected static final String API_HOST = "api.flickr.com";

    /**
     * Host is different for upload, need to set it from Uploader.java.
     */
    public static final String UPLOAD_API_HOST = "up.flickr.com";

    protected static final String DEFAULT_SCHEME = "https";
    
    private String transportType;

    protected Class<?> responseClass;

    private String path;

    private String host;

    private int port = 443;
    
    private String scheme;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transport) {
        this.transportType = transport;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Invoke an HTTP GET request on a remote host. You must close the InputStream after you are done with.
     * 
     * @param path
     *            The request path
     * @param parameters
     *            The parameters (collection of Parameter objects)
     * @param apiKey
     * @param sharedSecret
     * @return The Response
     * @throws FlickrException
     */
    public abstract Response get(String path, Map<String, Object> parameters, String apiKey, String sharedSecret) throws FlickrException;

    /**
     * Invoke an HTTP POST request on a remote host.
     * 
     * @param path
     *            The request path
     * @param parameters
     *            The parameters (List of Parameter objects)
     * @param apiKey
     * @param sharedSecret
     * @return The Response object
     * @throws FlickrException
     */
    public abstract Response post(String path, Map<String, Object> parameters, String apiKey, String sharedSecret) throws FlickrException;

    /**
     * Invoke an HTTP POST multipart request on a remote host.
     *
     * @param path
     *            The request path
     * @param parameters
     *            The parameters (List of Parameter objects)
     * @param payload
     * @param apiKey
     * @param sharedSecret
     * @return The Response object
     * @throws FlickrException
     */
    public abstract Response postMultiPart(String path, UploadMetaData parameters, Payload payload, String apiKey, String sharedSecret) throws FlickrException;

    /**
     * Invoke a non OAuth HTTP GET request on a remote host.
     * 
     * This is only used for the Flickr OAuth methods checkToken and getAccessToken.
     * 
     * @param path
     *            The request path
     * @param parameters
     *            The parameters
     * @return The Response
     * @throws FlickrException
     */
    public abstract Response getNonOAuth(String path, Map<String, String> parameters) throws FlickrRuntimeException;

    public void setResponseClass(Class<?> responseClass) {
        if (responseClass == null) {
            throw new IllegalArgumentException("The response Class cannot be null");
        }
        this.responseClass = responseClass;
    }
}
