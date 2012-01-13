/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Auth;
import junit.framework.TestCase;
import org.xml.sax.SAXException;

/**
 * @author Anthony Eden
 * @version $Id: FavoritesInterfaceTest.java,v 1.8 2008/01/26 00:05:17 x-mago Exp $
 */
public class FavoritesInterfaceTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
            properties.load(in);

            REST rest = new REST();

            flickr = new Flickr(
                properties.getProperty("apiKey"),
                properties.getProperty("secret"),
                rest
            );

            RequestContext requestContext = RequestContext.getRequestContext();

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetList() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection favorites = iface.getList(null, 0, 0, null);
        assertNotNull(favorites);
        assertEquals(2, favorites.size());
    }

    public void testGetListWithExtras() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection favorites = iface.getList(null, 0, 0, Extras.ALL_EXTRAS);
        assertNotNull(favorites);
        assertEquals(2, favorites.size());
    }

    public void testGetPublicList() throws FlickrException, IOException, SAXException {
        FavoritesInterface iface = flickr.getFavoritesInterface();
        Collection favorites = iface.getPublicList("77348956@N00", 0, 0, null);
        assertNotNull(favorites);
        assertEquals(6, favorites.size());
    }

    public void testAddAndRemove() throws FlickrException, IOException, SAXException {
        String photoId = "2153378";
        FavoritesInterface iface = flickr.getFavoritesInterface();
        iface.add(photoId);

        Photo foundPhoto = null;
        Iterator favorites = iface.getList(null, 0, 0, null).iterator();
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
