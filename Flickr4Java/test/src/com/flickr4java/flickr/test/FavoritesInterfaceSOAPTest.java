/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;

/**
 * @author Anthony Eden
 */
public class FavoritesInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, FlickrException {
        testProperties = new TestProperties();

        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
        Flickr.debugStream = true;
        Flickr.debugRequest = true;
        SOAP soap = new SOAP();

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
    public void testGetList() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getList(null, 0, 0, null);
        assertNotNull(favorites);
        assertEquals(1, favorites.size());
    }

    @Ignore
    @Test
    public void testGetListWithExtras() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getList(null, 0, 0, Extras.ALL_EXTRAS);
        assertNotNull(favorites);
        assertEquals(1, favorites.size());
    }

    @Ignore
    @Test
    public void testGetPublicList() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getPublicList("77348956@N00", 0, 0, null);
        assertNotNull(favorites);
        assertEquals(1, favorites.size());
    }

    @Ignore
    @Test
    public void testAddAndRemove() throws FlickrException {
        String photoId = "2153378";
        FavoritesInterface iface = flickr.getFavoritesInterface();
        iface.add(photoId);

        Photo foundPhoto = null;
        Iterator<Photo> favorites = iface.getList(null, 0, 0, null).iterator();
        while (favorites.hasNext()) {
            Photo photo = (Photo) favorites.next();
            if (photo.getId().equals(photoId)) {
                foundPhoto = photo;
                break;
            }
        }
        assertNotNull(foundPhoto);
        assertEquals(photoId, foundPhoto.getId());
        iface.remove(photoId);
    }

}
