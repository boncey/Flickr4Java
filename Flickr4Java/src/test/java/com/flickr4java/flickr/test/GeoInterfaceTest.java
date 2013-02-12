package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.photos.geo.GeoPermissions;

/**
 * 
 * @author till
 * @version $Id: GeoInterfaceTest.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class GeoInterfaceTest extends Flickr4JavaTest {

    @Before
    public void setUp() throws FlickrException {
        super.setUp();
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

    private void setGeoParameters(String photoId) {
        GeoInterface geo = flickr.getPhotosInterface().getGeoInterface();
        GeoData location = new GeoData();
        location.setLatitude(23.34f);
        location.setLongitude(46.99f);
        location.setAccuracy(13);
        try {
            geo.setLocation(photoId, location);
        } catch (FlickrException e) {
            fail(e.getMessage());
        }
    }

}
