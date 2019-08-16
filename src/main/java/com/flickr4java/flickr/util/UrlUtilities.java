

package com.flickr4java.flickr.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/** 
 * @author Anthony Eden 
 * @author Mike Chaberski
 */
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
     * @deprecated use {@link #buildSecureUrl(java.lang.String, int, java.lang.String, java.util.Map) }
     */
    @Deprecated
    public static URL buildUrl(String host, int port, String path, Map<String, String> parameters) throws MalformedURLException {
        return buildUrl("http", port, path, parameters);
    }
    
    /**
     * Build a request URL using a given scheme.
     * 
     * @param scheme the scheme, either {@code http} or {@code https}
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
    public static URL buildUrl(String scheme, String host, int port, String path, Map<String, String> parameters) throws MalformedURLException {
        checkSchemeAndPort(scheme, port);
        StringBuilder buffer = new StringBuilder();
        if (!host.startsWith(scheme + "://")) {
            buffer.append(scheme).append("://");
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
    public static URL buildSecureUrl(String host, int port, String path, Map<String, String> parameters) throws MalformedURLException {
        return buildUrl("https", host, port, path, parameters);
    }

    public static URL buildSecurePostUrl(String host, int port, String path) throws MalformedURLException {
        return buildPostUrl("https", host, port, path);
    }
    
    private static void checkScheme(String scheme) {
        if (scheme == null || !("http".equals(scheme) || "https".equals(scheme))) {
            throw new IllegalArgumentException("scheme must be http or https");
        }
    }
    
    private static void checkSchemeAndPort(String scheme, int port) {
        checkScheme(scheme);
        /*
         * Be liberal about accepting non-default ports, but strict
         * about mismatching scheme and default port.
         */
        if ("http".equals(scheme) && port == 443) {
            throw new IllegalArgumentException("port 443 is invalid with http scheme");
        }
        if ("https".equals(scheme) && port == 80) {
            throw new IllegalArgumentException("port 80 is invalid with https scheme");
        }
    }
    
    /**
     * Build a POST URL with {@code http} scheme.
     * @param host the host
     * @param port the port
     * @param path the path
     * @return
     * @throws MalformedURLException 
     */
    public static URL buildPostUrl(String host, int port, String path) throws MalformedURLException {
        return buildPostUrl("http", host, port, path);
    }
    
    public static URL buildPostUrl(String scheme, String host, int port, String path) throws MalformedURLException {
        checkSchemeAndPort(scheme, port);
        StringBuilder buffer = new StringBuilder();
        buffer.append(scheme).append("://");
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
     * Construct the BuddyIconUrl with {@code http} scheme.
     * <p>
     * If none available, return the <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @param iconFarm
     * @param iconServer
     * @param id
     * @return The BuddyIconUrl
     * @deprecated use {@link #createSecureBuddyIconUrl(int, int, java.lang.String) }
     */
    @Deprecated
    public static String createBuddyIconUrl(int iconFarm, int iconServer, String id) {
        return createBuddyIconUrl("http", iconFarm, iconServer, id);
    }
    
    /**
     * Construct the BuddyIconUrl with {@code https} scheme.
     * <p>
     * If none available, return the <a href="https://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @param iconFarm
     * @param iconServer
     * @param id
     * @return The BuddyIconUrl
     */
    public static String createSecureBuddyIconUrl(int iconFarm, int iconServer, String id) {
        return createBuddyIconUrl("https", iconFarm, iconServer, id);
    }
    
    public static String createBuddyIconUrl(String scheme, int iconFarm, int iconServer, String id) {
        checkScheme(scheme);
        /**
         * The default-URL, if the iconServer equals 0.
         */
        String iconUrl = scheme + "://www.flickr.com/images/buddyicon.jpg";
        if (iconServer > 0) {
            iconUrl = scheme + "://farm" + iconFarm + ".staticflickr.com/" + iconServer + "/buddyicons/" + id + ".jpg";
        }
        return iconUrl;
    }

}
