package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Anthony Eden
 */
public class PhotosetsInterfaceSOAPTest {

    Flickr flickr = null;
    Properties properties = null;

    @Before
    public void setUp() throws ParserConfigurationException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            SOAP soap = new SOAP(properties.getProperty("host"));
            flickr = new Flickr(properties.getProperty("apiKey"), soap);

        } finally {
            IOUtilities.close(in);
        }
    }

    @Ignore
    @Test
    public void testCreateAndDelete() throws FlickrException, IOException, SAXException {
        RequestContext requestContext = RequestContext.getRequestContext();
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.create("test", "A test photoset", properties.getProperty("photoid"));
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
    public void testGetContext() throws FlickrException, IOException, SAXException {
        RequestContext requestContext = RequestContext.getRequestContext();
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoContext photoContext = iface.getContext(properties.getProperty("photoid"), properties.getProperty("photosetid"));
        Photo previousPhoto = photoContext.getPreviousPhoto();
        Photo nextPhoto = photoContext.getNextPhoto();
        assertNotNull(previousPhoto);
        assertNotNull(nextPhoto);
    }

    @Ignore
    @Test
    public void testGetInfo() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.getInfo(properties.getProperty("photosetid"));
        assertNotNull(photoset);
        assertEquals(3, photoset.getPhotoCount());
    }

    @Ignore
    @Test
    public void testGetList() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList(properties.getProperty("nsid"));
        assertNotNull(photosets);
        assertEquals(1, photosets.getPhotosets().size());
    }

    @Ignore
    @Test
    public void testGetList2() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList("26095690@N00");
        assertNotNull(photosets);
    }

    @Ignore
    @Test
    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoList photos = iface.getPhotos(
            properties.getProperty("photosetid"),
            10,
            1
        );
        assertNotNull(photos);
        assertEquals(3, photos.size());
        assertEquals("javatest", ((Photo) photos.get(0)).getOwner().getUsername()); 
    }

    @Ignore
    @Test
    public void testOrderSets() throws FlickrException, IOException, SAXException {
        RequestContext requestContext = RequestContext.getRequestContext();
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        String[] photosetIds = {properties.getProperty("photosetid")};
        iface.orderSets(photosetIds);
    }

}
