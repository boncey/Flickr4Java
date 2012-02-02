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

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author till
 * @version $Id: GeoInterfaceTest.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class GeoInterfaceTest {

    Flickr flickr = null;
    private TestProperties testProperties;

    @Before
    public void setUp() throws Exception {
        testProperties = new TestProperties();

        REST rest = new REST();

        flickr = new Flickr(
                testProperties.getApiKey(),
                testProperties.getSecret(),
                rest
                );

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);

        setGeoParameters(testProperties.getGeoWritePhotoId());
    }

    @Test
    public void testGetLocation() throws FlickrException {
        String photoId = testProperties.getGeoWritePhotoId();
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoData location = geo.getLocation(photoId);
        assertNotNull(location);
        assertTrue(location.getLatitude() > 0);
        assertTrue(location.getLongitude() > 0);
        assertTrue(location.getAccuracy() >= 1);
        assertTrue(location.getAccuracy() <= 16);
    }

    @Test
    public void testGetPerms() throws FlickrException {
        String photoId = testProperties.getGeoWritePhotoId();
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoPermissions perms = geo.getPerms(photoId);
        assertNotNull(perms);
        assertTrue(perms.isPublic());
        assertFalse(perms.isContact());
        assertFalse(perms.isFriend());
        assertFalse(perms.isFamily());
    }

    @Test
    public void testSetLocation() throws FlickrException {
        String photoId = testProperties.getGeoWritePhotoId();
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

    private void setGeoParameters(String photoId) throws FlickrException {
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoData location = new GeoData();
        location.setLatitude(23.34f);
        location.setLongitude(46.99f);
        location.setAccuracy(13);
        geo.setLocation(photoId, location);
    }

}
