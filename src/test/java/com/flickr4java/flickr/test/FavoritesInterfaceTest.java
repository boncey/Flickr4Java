

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;

import org.junit.Test;

import java.util.Collection;

/**
 * @author Anthony Eden
 * @version $Id: FavoritesInterfaceTest.java,v 1.8 2008/01/26 00:05:17 x-mago Exp $
 */
public class FavoritesInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetList() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getList(null, 15, 1, null);
        assertNotNull(favorites);
        assertFalse(favorites.isEmpty());
    }

    @Test
    public void testGetListWithExtras() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getList(null, 15, 1, Extras.ALL_EXTRAS);
        assertNotNull(favorites);
        assertFalse(favorites.isEmpty());
    }

    @Test
    public void testGetPublicList() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection<Photo> favorites = iface.getPublicList(testProperties.getNsid(), 0, 0, null);
        assertNotNull(favorites);
        assertTrue(favorites.size() > 0);
    }

    @Test
    public void testAddAndRemove() throws FlickrException {
        String photoId = "2153378";
        FavoritesInterface iface = flickr.getFavoritesInterface();

        try {
            iface.remove(photoId);
        } catch (Exception e) {
            // running the remove in case it's there before the add
        }
        iface.add(photoId);  // No response to check

        iface.remove(photoId);
    }

    @Test
    public void testGetContext() throws FlickrException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        PhotoContext context = iface.getContext("5844052737", "77348956@N00");
        assertNotNull(context);
        assertEquals("5602817067", context.getPreviousPhoto().getId());
        assertEquals("5844052415", context.getNextPhoto().getId());
    }

}
