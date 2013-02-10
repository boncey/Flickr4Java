/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.urls.UrlsInterface;

/**
 * @author Anthony Eden
 */
public class UrlsInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(testProperties.getApiKey()).apiSecret(testProperties.getSecret()).build();
        SOAP soap = new SOAP(service);
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Ignore
    @Test
    public void testGetGroup() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getGroup(testProperties.getGroupId());
        assertEquals("http://www.flickr.com/groups/central/", url);
    }

    @Ignore
    @Test
    public void testGetUserPhotos() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserPhotos(testProperties.getNsid());
        String username = testProperties.getUsername();
        assertEquals(String.format("http://www.flickr.com/photos/%s/", username), url);
    }

    @Ignore
    @Test
    public void testGetUserProfile() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserProfile(testProperties.getNsid());
        String username = testProperties.getUsername();
        assertEquals(String.format("http://www.flickr.com/people/%s/", username), url);
    }

    @Ignore
    @Test
    public void testLookupGroup() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        Group group = iface.lookupGroup("http://www.flickr.com/groups/central/");
        assertEquals("FlickrCentral", group.getName());
        assertEquals("34427469792@N01", group.getId());
    }

    @Ignore
    @Test
    public void testLookupUser() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String username = testProperties.getUsername();
        String usernameOnFlickr = iface.lookupUser(String.format("http://www.flickr.com/people/%s/", username));
        assertEquals(username, usernameOnFlickr);
    }

}
