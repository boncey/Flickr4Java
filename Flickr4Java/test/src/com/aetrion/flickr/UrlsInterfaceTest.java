/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.urls.UrlsInterface;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class UrlsInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), rest);

            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setSharedSecret(properties.getProperty("secret"));

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetGroup() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getGroup(properties.getProperty("groupid"));
        assertEquals("http://www.flickr.com/groups/central/", url);
    }

    public void testGetUserPhotos() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserPhotos(properties.getProperty("nsid"));
        assertEquals("http://www.flickr.com/photos/javatest/", url);
    }

    public void testGetUserProfile() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserProfile(properties.getProperty("nsid"));
        assertEquals("http://www.flickr.com/people/javatest/", url);
    }

    public void testLookupGroup() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        Group group = iface.lookupGroup("http://www.flickr.com/groups/central/");
        assertEquals("FlickrCentral", group.getName());
        assertEquals("34427469792@N01", group.getId());
    }

    public void testLookupUser() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String username = iface.lookupUser("http://www.flickr.com/people/javatest");
        assertEquals("javatest", username);
    }

}
