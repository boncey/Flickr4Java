/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.util.Base64;
import com.flickr4java.flickr.util.DebugInputStream;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.util.UrlUtilities;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Transport implementation using the REST interface.
 *
 * @author Anthony Eden
 * @version $Id: REST.java,v 1.26 2009/07/01 22:07:08 x-mago Exp $
 */
public class REST extends Transport {

    public static final String PATH = "/services/rest/";
    private boolean proxyAuth = false;
    private String proxyUser = "";
    private String proxyPassword = "";
    private final DocumentBuilder builder;
    private static Object mutex = new Object();

    /**
     * Construct a new REST transport instance.
     */
    public REST() {
        setTransportType(REST);
        setHost(Flickr.DEFAULT_HOST);
        setPath(PATH);
        setResponseClass(RESTResponse.class);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construct a new REST transport instance using the specified host endpoint.
     *
     * @param host The host endpoint
     */
    public REST(String host) {
        this();
        setHost(host);
    }

    /**
     * Construct a new REST transport instance using the specified host and port endpoint.
     *
     * @param host The host endpoint
     * @param port The port
     */
    public REST(String host, int port) {
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
     * @throws FlickrException
     */
    @Override
    public com.flickr4java.flickr.Response get(String path, Map<String, String> parameters, String sharedSecret) throws FlickrException {

        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.flickr.com/services/rest");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            request.addQuerystringParameter(entry.getKey(), entry.getValue());
        }

        if (proxyAuth) {
            request.addHeader("Proxy-Authorization", "Basic " + getProxyCredentials());
        }

        RequestContext requestContext = RequestContext.getRequestContext();
        Auth auth = requestContext.getAuth();
        Token requestToken = new Token(auth.getToken(), auth.getTokenSecret());
        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(parameters.get(Flickr.API_KEY))
                .apiSecret(sharedSecret).build();
        service.signRequest(requestToken, request);

        if (Flickr.debugRequest) {
            System.out.println("GET: " + request.getCompleteUrl());
        }
        org.scribe.model.Response scribeResponse = request.send();

        try {

            com.flickr4java.flickr.Response response = null;
            synchronized (mutex) {
                String strXml = scribeResponse.getBody();
                if(Flickr.debugStream) {
                    System.out.println(strXml);
                }
                Document document = builder.parse(new InputSource(new StringReader(strXml)));
                response = (com.flickr4java.flickr.Response) responseClass.newInstance();
                response.parse(document);
            }
            return response;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new FlickrException(e);
        } catch (IOException e) {
            throw new FlickrException(e);
        }
    }

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
    @Override
    public Response getNonOAuth(String path, Map<String, String> parameters) throws FlickrException {
        InputStream in = null;
        try {
            URL url = UrlUtilities.buildUrl(getHost(), getPort(), path, parameters);
            if (Flickr.debugRequest) {
                System.out.println("GET: " + url);
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (proxyAuth) {
                conn.setRequestProperty(
                        "Proxy-Authorization",
                        "Basic " + getProxyCredentials()
                        );
            }
            conn.connect();

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
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new FlickrException(e);
        } catch (SAXException e) {
            throw new FlickrException(e);
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
     * @throws FlickrException
     */
    @Override
    public com.flickr4java.flickr.Response post(String path, Map<String, String> parameters, String sharedSecret, boolean multipart) throws FlickrException {

        OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.flickr.com" + PATH);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            request.addQuerystringParameter(entry.getKey(), entry.getValue());
        }
        RequestContext requestContext = RequestContext.getRequestContext();
        Auth auth = requestContext.getAuth();
        Token requestToken = new Token(auth.getToken(), auth.getTokenSecret());
        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(parameters.get("apiKey"))
                .apiSecret(sharedSecret).build();
        service.signRequest(requestToken, request);

        if (proxyAuth) {
            request.addHeader("Proxy-Authorization", "Basic " + getProxyCredentials());
        }

        if (Flickr.debugRequest) {
            System.out.println("GET: " + request.getCompleteUrl());
        }

        org.scribe.model.Response scribeResponse = request.send();

        try {
            com.flickr4java.flickr.Response response = null;
            synchronized (mutex) {
                String strXml = scribeResponse.getBody();
                if (Flickr.debugStream) {
                    System.out.println(strXml);
                }
                Document document = builder.parse(new InputSource(new StringReader(strXml)));
                response = (com.flickr4java.flickr.Response) responseClass.newInstance();
                response.parse(document);
            }
            return response;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new FlickrException(e);
        } catch (IOException e) {
            throw new FlickrException(e);
        }
    }

    @SuppressWarnings("unused")
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
