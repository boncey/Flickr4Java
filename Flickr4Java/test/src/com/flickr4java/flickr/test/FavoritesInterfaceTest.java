/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Anthony Eden
 * @version $Id: FavoritesInterfaceTest.java,v 1.8 2008/01/26 00:05:17 x-mago Exp $
 */
public class FavoritesInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        testProperties = new TestProperties();
        REST rest = new REST();

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testGetList() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getList(null, 15, 1, null);
        assertNotNull(favorites);
        assertFalse(favorites.isEmpty());
    }

    @Test
    public void testGetListWithExtras() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getList(null, 15, 1, Extras.ALL_EXTRAS);
        assertNotNull(favorites);
        assertFalse(favorites.isEmpty());
    }

    @Test
    public void testGetPublicList() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getPublicList("77348956@N00", 0, 0, null);
        assertNotNull(favorites);
        assertEquals(14, favorites.size());
    }

    @Test
    public void testAddAndRemove() throws FlickrException, IOException, SAXException {
        String photoId = "2153378";
        FavoritesInterface iface = flickr.getFavoritesInterface();

        try {
            iface.remove(photoId);
        } catch (Exception e) {
            // running the remove in case it's there before the add
        }
        iface.add(photoId);

        Photo foundPhoto = null;
        Iterator<Photo> favorites = iface.getList(null, 0, 0, null).iterator();
        while (favorites.hasNext()) {
            Photo photo = favorites.next();
            if (photo.getId().equals(photoId)) {
                foundPhoto = photo;
                break;
            }
        }
        assertNotNull(foundPhoto);
        assertEquals(photoId, foundPhoto.getId());
        iface.remove(photoId);
    }

    @Test
    public void testGetContext() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        PhotoContext context = iface.getContext("5844052737", "77348956@N00");
        assertNotNull(context);
        assertEquals("5602817067", context.getPreviousPhoto().getId());
        assertEquals("5844052415", context.getNextPhoto().getId());
    }

}
