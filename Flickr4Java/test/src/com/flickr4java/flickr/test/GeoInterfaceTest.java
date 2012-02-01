package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.photos.geo.GeoPermissions;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author till
 * @version $Id: GeoInterfaceTest.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class GeoInterfaceTest {

    Flickr flickr = null;
    Properties properties = null;

    @Before
    public void setUp() throws Exception {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();

            flickr = new Flickr(
                    properties.getProperty("apiKey"),
                    properties.getProperty("secret"),
                    rest
                    );

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

    @Test
    public void testGetLocation() throws IOException, SAXException, FlickrException {
        String photoId = "240935723";
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoData location = geo.getLocation(photoId);
        assertNotNull(location);
        assertTrue(location.getLatitude() > 0);
        assertTrue(location.getLongitude() > 0);
        assertTrue(location.getAccuracy() >= 1);
        assertTrue(location.getAccuracy() <= 16);
    }

    @Test
    public void testGetPerms() throws IOException, SAXException, FlickrException {
        String photoId = properties.getProperty("geo.write.photoid");
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoPermissions perms = geo.getPerms(photoId);
        assertNotNull(perms);
        assertTrue(perms.isPublic());
        assertFalse(perms.isContact());
        assertFalse(perms.isFriend());
        assertFalse(perms.isFamily());
    }

    @Test
    public void testSetLocation() throws IOException, SAXException, FlickrException {
        String photoId = properties.getProperty("geo.write.photoid");
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoData location = new GeoData();
        location.setLatitude(23.34f);
        location.setLongitude(46.99f);
        location.setAccuracy(13);
        geo.setLocation(photoId, location);
        GeoData newLocation = geo.getLocation(photoId);
        assertEquals(location.getLatitude(), newLocation.getLatitude(), 0f);
        assertEquals(location.getLongitude(), newLocation.getLongitude(), 0f);
        assertEquals(location.getAccuracy(), newLocation.getAccuracy(), 0f);
        geo.removeLocation(photoId);
    }

}
