/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import org.xml.sax.SAXException;

import java.io.IOException;
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
    protected Class responseClass;
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
     * @return The Response
     * @throws IOException
     * @throws SAXException
     */
    public abstract Response get(String path, Map<String, String> parameters) throws IOException, SAXException;

    /**
     * Invoke an HTTP POST request on a remote host.
     *
     * @param path The request path
     * @param parameters The parameters (collection of Parameter objects)
     * @return The Response object
     * @throws IOException
     * @throws SAXException
     */
    public Response post(String path, Map<String, String> parameters) throws IOException, SAXException {
        return post(path, parameters, false);
    }

    /**
     * Invoke an HTTP POST request on a remote host.
     *
     * @param path The request path
     * @param parameters The parameters (List of Parameter objects)
     * @param multipart Use multipart
     * @return The Response object
     * @throws IOException
     * @throws SAXException
     */
    public abstract Response post(String path, Map<String, String> parameters, boolean multipart) throws IOException,
    SAXException;

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

    public Class getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class responseClass) {
        if (responseClass == null) {
            throw new IllegalArgumentException("The response Class cannot be null");
        }
        this.responseClass = responseClass;
    }

}
