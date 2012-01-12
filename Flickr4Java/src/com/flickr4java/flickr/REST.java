/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.util.Base64;
import com.flickr4java.flickr.util.DebugInputStream;
import com.flickr4java.flickr.util.DebugOutputStream;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.util.UrlUtilities;

/**
 * Transport implementation using the REST interface.
 *
 * @author Anthony Eden
 * @version $Id: REST.java,v 1.26 2009/07/01 22:07:08 x-mago Exp $
 */
public class REST extends Transport {

    private static final String UTF8 = "UTF-8";
    public static final String PATH = "/services/rest/";
    private boolean proxyAuth = false;
    private String proxyUser = "";
    private String proxyPassword = "";
    private DocumentBuilder builder;
    private static Object mutex = new Object();

    /**
     * Construct a new REST transport instance.
     *
     * @throws ParserConfigurationException
     */
    public REST() throws ParserConfigurationException {
        setTransportType(REST);
        setHost(Flickr.DEFAULT_HOST);
        setPath(PATH);
        setResponseClass(RESTResponse.class);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
    }

    /**
     * Construct a new REST transport instance using the specified host endpoint.
     *
     * @param host The host endpoint
     * @throws ParserConfigurationException
     */
    public REST(String host) throws ParserConfigurationException {
        this();
        setHost(host);
    }

    /**
     * Construct a new REST transport instance using the specified host and port endpoint.
     *
     * @param host The host endpoint
     * @param port The port
     * @throws ParserConfigurationException
     */
    public REST(String host, int port) throws ParserConfigurationException {
        this();
        setHost(host);
        setPort(port);
    }

    /**
     * Set a proxy for REST-requests.
     *
     * @param proxyHost
     * @param proxyPort
     */
    public void setProxy(String proxyHost, int proxyPort) {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", "" + proxyPort);
    }

    /**
     * Set a proxy with authentication for REST-requests.
     *
     * @param proxyHost
     * @param proxyPort
     * @param username
     * @param password
     */
    public void setProxy(
        String proxyHost, int proxyPort,
        String username, String password
    ) {
        setProxy (proxyHost, proxyPort);
        proxyAuth = true;
        proxyUser = username;
        proxyPassword = password;
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
    public Response get(String path, List parameters) throws IOException, SAXException {
        URL url = UrlUtilities.buildUrl(getHost(), getPort(), path, parameters);
        if (Flickr.debugRequest) System.out.println("GET: " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (proxyAuth) {
            conn.setRequestProperty(
                "Proxy-Authorization",
                "Basic " + getProxyCredentials()
            );
        }
        conn.connect();

        InputStream in = null;
        try {
            if (Flickr.debugStream) {
                in = new DebugInputStream(conn.getInputStream(), System.out);
            } else {
                in = conn.getInputStream();
            }

            Response response = null;
            synchronized (mutex) {
                Document document = builder.parse(in);
                response = (Response) responseClass.newInstance();
                response.parse(document);
            }
            return response;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: Replace with a better exception
        } catch (InstantiationException e) {
            throw new RuntimeException(e); // TODO: Replace with a better exception
        } finally {
            IOUtilities.close(in);
        }
    }

    /**
     * Invoke an HTTP POST request on a remote host.
     *
     * @param path The request path
     * @param parameters The parameters (collection of Parameter objects)
     * @param multipart Use multipart
     * @return The Response object
     * @throws IOException
     * @throws SAXException
     */
    public Response post(String path, List parameters, boolean multipart) throws IOException, SAXException {
        // see: AuthUtilities.getSignature()
        //AuthUtilities.addAuthToken(parameters);

        RequestContext requestContext = RequestContext.getRequestContext();
        URL url = UrlUtilities.buildPostUrl(getHost(), getPort(), path);

        HttpURLConnection conn = null;
        try {
            String boundary = "---------------------------7d273f7a0d3";

            conn = (HttpURLConnection) url.openConnection();

            if (proxyAuth) {
                conn.setRequestProperty(
                    "Proxy-Authorization",
                    "Basic " + getProxyCredentials()
                );
            }
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            if (multipart) {
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            }
            conn.connect();

            DataOutputStream out = null;
            try {
                if (Flickr.debugRequest) {
                    out = new DataOutputStream(
                        new DebugOutputStream(
                            conn.getOutputStream(),
                            System.out
                        )
                    );
                } else {
                    out = new DataOutputStream(conn.getOutputStream());
                }

                // construct the body
                if (multipart) {
                    out.writeBytes("--" + boundary + "\r\n");
                    Iterator iter = parameters.iterator();
                    while (iter.hasNext()) {
                        Parameter p = (Parameter) iter.next();
                        
                        writeParam(p.getName(), p.getValue(), out, boundary);
                    }
/*                    Auth auth = requestContext.getAuth();
                    if (auth != null) {
                        writeParam(
                            "api_sig",
                            AuthUtilities.getMultipartSignature(sharedSecret, parameters),
                            out,
                            boundary
                        );
                    } */
                } else {
                    Iterator iter = parameters.iterator();
                    while (iter.hasNext()) {
                        Parameter p = (Parameter) iter.next();
                        out.writeBytes(p.getName());
                        out.writeBytes("=");
                        try {
                            out.writeBytes(
                                URLEncoder.encode(
                                    String.valueOf(p.getValue()),
                                    UTF8
                                )
                            );
                        } catch (UnsupportedEncodingException e) {
                            // Should never happen, but just in case
                        }
                        if (iter.hasNext()) {
                            out.writeBytes("&");
                        }
                    }

                    Auth auth = requestContext.getAuth();
                    if (auth != null) {
                        // will be added at AuthUtilities.addAuthToken()
                        //out.writeBytes("&auth_token=");
                        //out.writeBytes(auth.getToken());
                        //out.writeBytes("&api_sig=");
                        //out.writeBytes(AuthUtilities.getSignature(parameters));
                    }
                }
                out.flush();
            } finally {
                IOUtilities.close(out);
            }

            InputStream in = null;
            try {
                if (Flickr.debugStream) {
                    in = new DebugInputStream(conn.getInputStream(), System.out);
                } else {
                    in = conn.getInputStream();
                }
                Response response = null;
                synchronized (mutex) {
                    Document document = builder.parse(in);
                    response = (Response) responseClass.newInstance();
                    response.parse(document);
                }
                return response;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e); // TODO: Replace with a better exception
            } catch (InstantiationException e) {
                throw new RuntimeException(e); // TODO: Replace with a better exception
            } finally {
                IOUtilities.close(in);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void writeParam(String name, Object value, DataOutputStream out, String boundary)
            throws IOException {
        if (value instanceof InputStream) {
            out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"image.jpg\";\r\n");
            out.writeBytes("Content-Type: image/jpeg" + "\r\n\r\n");
            InputStream in = (InputStream) value;
            byte[] buf = new byte[512];
            int res = -1;
            while ((res = in.read(buf)) != -1) {
                out.write(buf);
            }
            out.writeBytes("\r\n" + "--" + boundary + "\r\n");
        } else if (value instanceof byte[]) {
            out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"image.jpg\";\r\n");
            out.writeBytes("Content-Type: image/jpeg" + "\r\n\r\n");
            out.write((byte[]) value);
            out.writeBytes("\r\n" + "--" + boundary + "\r\n");
        } else {
            out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
            out.write(((String) value).getBytes("UTF-8"));
            out.writeBytes("\r\n" + "--" + boundary + "\r\n");
        }
    }

    public boolean isProxyAuth() {
        return proxyAuth;
    }

    /**
     * Generates Base64-encoded credentials from locally stored
     * username and password.
     *
     * @return credentials
     */
    public String getProxyCredentials() {
        return new String(
            Base64.encode((proxyUser + ":" + proxyPassword).getBytes())
        );
    }
}
