package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.photos.geo.GeoPermissions;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * 
 * @author till
 * @version $Id: GeoInterfaceTest.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class GeoInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public GeoInterfaceTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
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

            RequestContext requestContext = RequestContext.getRequestContext();

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

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

    public void testGetPerms() throws IOException, SAXException, FlickrException {
        String photoId = "419231219";
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoPermissions perms = geo.getPerms(photoId);
        assertNotNull(perms);
        assertTrue(perms.isPublic());
        assertFalse(perms.isContact());
        assertFalse(perms.isFriend());
        assertFalse(perms.isFamily());
    }

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
