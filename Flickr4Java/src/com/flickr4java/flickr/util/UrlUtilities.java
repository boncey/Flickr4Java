/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/** @author Anthony Eden */
public class UrlUtilities {

    public static final String UTF8 = "UTF-8";

    /**
     * Build a request URL.
     * 
     * @param host
     *            The host
     * @param port
     *            The port
     * @param path
     *            The path
     * @param parameters
     *            The parameters
     * @return The URL
     * @throws MalformedURLException
     */
    public static URL buildUrl(String host, int port, String path, Map<String, String> parameters) throws MalformedURLException {
        // see: AuthUtilities.getSignature()
        // AuthUtilities.addAuthToken(parameters);

        StringBuffer buffer = new StringBuffer();
        if (!host.startsWith("http://")) {
            buffer.append("http://");
        }
        buffer.append(host);
        if (port > 0) {
            buffer.append(':');
            buffer.append(port);
        }
        if (path == null) {
            path = "/";
        }
        buffer.append(path);

        if (!parameters.isEmpty()) {
            buffer.append('?');
        }
        int size = parameters.size();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append('=');
            Object value = entry.getValue();
            if (value != null) {
                String string = value.toString();
                try {
                    string = URLEncoder.encode(string, UTF8);
                } catch (UnsupportedEncodingException e) {
                    // Should never happen, but just in case
                }
                buffer.append(string);
            }
            if (--size != 0) {
                buffer.append('&');
            }
        }

        /*
         * RequestContext requestContext = RequestContext.getRequestContext(); Auth auth = requestContext.getAuth(); if (auth != null &&
         * !ignoreMethod(getMethod(parameters))) { buffer.append("&api_sig="); buffer.append(AuthUtilities.getSignature(sharedSecret, parameters)); }
         */

        return new URL(buffer.toString());
    }

    public static URL buildPostUrl(String host, int port, String path) throws MalformedURLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("http://");
        buffer.append(host);
        if (port > 0) {
            buffer.append(':');
            buffer.append(port);
        }
        if (path == null) {
            path = "/";
        }
        buffer.append(path);
        return new URL(buffer.toString());
    }

    /**
     * Construct the BuddyIconUrl.
     * <p>
     * If none available, return the <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @param iconFarm
     * @param iconServer
     * @param id
     * @return The BuddyIconUrl
     */
    public static String createBuddyIconUrl(int iconFarm, int iconServer, String id) {
        /**
         * The default-URL, if the iconServer equals 0.
         */
        String iconUrl = "http://www.flickr.com/images/buddyicon.jpg";
        if (iconServer > 0) {
            iconUrl = "http://farm" + iconFarm + ".static.flickr.com/" + iconServer + "/buddyicons/" + id + ".jpg";
        }
        return iconUrl;
    }

}
