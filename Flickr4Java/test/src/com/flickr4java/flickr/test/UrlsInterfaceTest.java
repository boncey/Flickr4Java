/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.urls.UrlsInterface;
import com.flickr4java.flickr.util.IOUtilities;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Anthony Eden
 */
public class UrlsInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    @Override
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), rest);

            Auth auth = new Auth();
            auth.setPermission(Permission.READ);
            auth.setToken(properties.getProperty("token"));
            auth.setTokenSecret(properties.getProperty("tokensecret"));

            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setAuth(auth);
            flickr.setAuth(auth);
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
        String username = properties.getProperty("username");
        assertEquals(String.format("http://www.flickr.com/photos/%s/", username), url);
    }

    public void testGetUserProfile() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserProfile(properties.getProperty("nsid"));
        String username = properties.getProperty("username");
        assertEquals(String.format("http://www.flickr.com/people/%s/", username), url);
    }

    public void testLookupGroup() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        Group group = iface.lookupGroup("http://www.flickr.com/groups/central/");
        assertEquals("FlickrCentral", group.getName());
        assertEquals("34427469792@N01", group.getId());
    }

    public void testLookupUser() throws FlickrException, IOException, SAXException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String username = properties.getProperty("username");
        String usernameOnFlickr = iface.lookupUser(String.format("http://www.flickr.com/people/%s/", username));
        assertEquals(username, usernameOnFlickr);
    }

}
