/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import java.util.Map;

/**
 * The abstract Transport class provides a common interface for transporting requests to the Flickr servers. Flickr
 * offers several transport methods including REST, SOAP and XML-RPC. FlickrJ currently implements the REST transport
 * and work is being done to include the SOAP transport.
 *
 * @author Matt Ray
 * @author Anthony Eden
 */
public abstract class Transport {

    public static final String REST = "REST";
    public static final String SOAP = "SOAP";

    protected static final String API_HOST = "http://api.flickr.com";
    private String transportType;
    protected Class<?> responseClass;
    private String path;
    private String host;
    private int port = 80;

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

    /**
     * Invoke an HTTP GET request on a remote host.  You must close the InputStream after you are done with.
     *
     * @param path The request path
     * @param parameters The parameters (collection of Parameter objects)
     * @param sharedSecret TODO
     * @return The Response
     * @throws FlickrException
     */
    public abstract Response get(String path, Map<String, String> parameters, String sharedSecret) throws FlickrException;

    /**
     * Invoke an HTTP POST request on a remote host.
     *
     * @param path The request path
     * @param parameters The parameters (collection of Parameter objects)
     * @param sharedSecret TODO
     * @return The Response object
     * @throws FlickrException
     */
    public Response post(String path, Map<String, String> parameters, String sharedSecret) throws FlickrException {
        return post(path, parameters, sharedSecret, false);
    }

    /**
     * Invoke an HTTP POST request on a remote host.
     *
     * @param path The request path
     * @param parameters The parameters (List of Parameter objects)
     * @param sharedSecret TODO
     * @param multipart Use multipart
     * @return The Response object
     * @throws FlickrException
     */
    public abstract Response post(String path, Map<String, String> parameters, String sharedSecret, boolean multipart) throws FlickrException;

    /**
     * Invoke a non OAuth HTTP GET request on a remote host.
     * 
     * This is only used for the Flickr OAuth methods checkToken and getAccessToken.
     *
     * @param path The request path
     * @param parameters The parameters
     * @return The Response
     * @throws FlickrException
     */
    public abstract Response getNonOAuth(String path, Map<String, String> parameters)
            throws FlickrException;

    /**
     * @return Returns the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<?> responseClass) {
        if (responseClass == null) {
            throw new IllegalArgumentException("The response Class cannot be null");
        }
        this.responseClass = responseClass;
    }

}
