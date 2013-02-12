/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.galleries.Gallery;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.urls.UrlsInterface;

/**
 * @author Anthony Eden
 */
public class UrlsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetGroup() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getGroup(testProperties.getGroupId());
        assertEquals("http://www.flickr.com/groups/central/", url);
    }

    @Test
    public void testGetUserPhotos() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserPhotos(testProperties.getNsid());
        String username = testProperties.getUsername();
        assertEquals(String.format("http://www.flickr.com/photos/%s/", username), url);
    }

    @Test
    public void testGetUserProfile() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String url = iface.getUserProfile(testProperties.getNsid());
        String username = testProperties.getUsername();
        assertEquals(String.format("http://www.flickr.com/people/%s/", username), url);
    }

    @Test
    public void testLookupGroup() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        Group group = iface.lookupGroup("http://www.flickr.com/groups/central/");
        assertEquals("FlickrCentral", group.getName());
        assertEquals("34427469792@N01", group.getId());
    }

    @Test
    public void testLookupUser() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();
        String username = testProperties.getUsername();
        String usernameOnFlickr = iface.lookupUser(String.format("http://www.flickr.com/people/%s/", username));
        assertEquals(username, usernameOnFlickr);
    }

    @Test
    public void testLookupGallery() throws FlickrException {
        UrlsInterface iface = flickr.getUrlsInterface();

        Gallery gallery = iface.lookupGallery(String.format("http://www.flickr.com/photos/%s/",
                testProperties.getUsername() + "/galleries/" + testProperties.getGalleryId()));
        assertNotNull(gallery);
        assertEquals("1979953-" + testProperties.getGalleryId(), gallery.getId());
    }

}
