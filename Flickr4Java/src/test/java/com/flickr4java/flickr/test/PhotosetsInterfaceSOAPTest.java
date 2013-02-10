package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

/**
 * @author Anthony Eden
 */
public class PhotosetsInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        SOAP soap = new SOAP(testProperties.getHost());
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

    }

    @Ignore
    @Test
    public void testCreateAndDelete() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.create("test", "A test photoset", testProperties.getPhotoId());
        assertNotNull(photoset);
        assertNotNull(photoset.getId());
        assertNotNull(photoset.getUrl());
        iface.delete(photoset.getId());
    }

    @Ignore
    @Test
    public void testEditMeta() {

    }

    @Ignore
    @Test
    public void testEditPhotos() {

    }

    @Ignore
    @Test
    public void testGetContext() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoContext photoContext = iface.getContext(testProperties.getPhotoId(), testProperties.getPhotosetId());
        Photo previousPhoto = photoContext.getPreviousPhoto();
        Photo nextPhoto = photoContext.getNextPhoto();
        assertNotNull(previousPhoto);
        assertNotNull(nextPhoto);
    }

    @Ignore
    @Test
    public void testGetInfo() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.getInfo(testProperties.getPhotosetId());
        assertNotNull(photoset);
        assertEquals(3, photoset.getPhotoCount());
    }

    @Ignore
    @Test
    public void testGetList() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList(testProperties.getNsid());
        assertNotNull(photosets);
        assertEquals(1, photosets.getPhotosets().size());
    }

    @Ignore
    @Test
    public void testGetList2() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList("26095690@N00");
        assertNotNull(photosets);
    }

    @Ignore
    @Test
    public void testGetPhotos() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoList<Photo> photos = iface.getPhotos(testProperties.getPhotosetId(), 10, 1);
        assertNotNull(photos);
        assertEquals(3, photos.size());
        assertEquals("javatest", photos.get(0).getOwner().getUsername());
    }

    @Ignore
    @Test
    public void testOrderSets() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        String[] photosetIds = { testProperties.getPhotosetId() };
        iface.orderSets(photosetIds);
    }

}
