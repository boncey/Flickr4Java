package com.flickr4java.flickr.test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.IOUtilities;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Anthony Eden
 */
public class PhotosetsInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    @Override
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        //Flickr.debugStream = true;

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

    public void testCreateAndDelete() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.create("test", "A test photoset", properties.getProperty("photoid"));
        assertNotNull(photoset);
        assertNotNull(photoset.getId());
        assertNotNull(photoset.getUrl());
        iface.delete(photoset.getId());
    }

    public void testEditMeta() {

    }

    public void testEditPhotos() {

    }

    public void testGetContext() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoContext photoContext = iface
                .getContext(properties.getProperty("photoid"), properties.getProperty("photosetid"));
        Photo previousPhoto = photoContext.getPreviousPhoto();
        Photo nextPhoto = photoContext.getNextPhoto();
        assertNotNull(previousPhoto);
        assertNotNull(nextPhoto);
    }

    public void testGetInfo() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.getInfo(properties.getProperty("photosetid"));
        assertNotNull(photoset);
        assertNotNull(photoset.getPrimaryPhoto());
        assertTrue(photoset.getPhotoCount() >= 1);
    }

    public void testGetList() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList(properties.getProperty("nsid"));
        assertNotNull(photosets);
        assertFalse(photosets.getPhotosets().isEmpty());
    }

    public void testGetList2() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList("26095690@N00");
        assertNotNull(photosets);
    }

    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoList photos = iface.getPhotos(
                properties.getProperty("photosetid"),
                10,
                1
                );
        assertNotNull(photos);
        assertTrue(photos.size() >= 1);
        assertEquals(properties.getProperty("username"), ((Photo) photos.get(0)).getOwner().getUsername());
        assertEquals(properties.getProperty("nsid"), ((Photo) photos.get(0)).getOwner().getId());
    }

    public void testOrderSets() throws FlickrException, IOException, SAXException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        String[] photosetIds = {properties.getProperty("photosetid")};
        iface.orderSets(photosetIds);
    }

}
