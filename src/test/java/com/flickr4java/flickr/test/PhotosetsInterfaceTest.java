package com.flickr4java.flickr.test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Anthony Eden
 */
public class PhotosetsInterfaceTest extends Flickr4JavaTest {

    private static Logger _log = LoggerFactory.getLogger(PhotosetsInterfaceTest.class);

    private Photoset testSet;

    private List<String> setPics;

    @Override
    @Before
    public void setUp() throws FlickrException {
        super.setUp();

        setPics = testProperties.getPhotosetPhotos();

        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        testSet = iface.create("PhotosetsInterfaceTest", "JUnit test, should be deleted", setPics.get(0));
        iface.addPhoto(testSet.getId(), setPics.get(1));
        iface.addPhoto(testSet.getId(), setPics.get(2));
    }

    @After
    public void tearDown() throws FlickrException {
        setAuth(Permission.DELETE);

        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        iface.delete(testSet.getId());
    }

    @Test
    public void testCreateAndDelete() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.create("test", "A test photoset", testProperties.getPhotoId());
        assertNotNull(photoset);
        assertNotNull(photoset.getId());
        assertNotNull(photoset.getUrl());
        iface.delete(photoset.getId());
    }

    @Test
    public void testEditMeta() {

    }

    @Test
    public void testEditPhotos() throws FlickrException {

        PhotosetsInterface iface = flickr.getPhotosetsInterface();

        iface.editPhotos(testSet.getId(), testProperties.getPhotoId(), setPics.toArray(new String[setPics.size()]));

        Photoset ps = iface.getInfo(testSet.getId());
        assertNotNull(ps);
        assertEquals(testProperties.getPhotoId(), ps.getPrimaryPhoto().getId());
        assertTrue(ps.getPhotoCount() >= 2);
    }

    @Test
    public void testGetContext() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoContext photoContext = iface.getContext(testProperties.getPhotoId(), testProperties.getPhotosetId());
        Photo previousPhoto = photoContext.getPreviousPhoto();
        Photo nextPhoto = photoContext.getNextPhoto();
        assertNotNull(previousPhoto);
        assertNotNull(nextPhoto);
    }

    @Test
    public void testGetInfo() throws FlickrException {
        clearAuth();

        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.getInfo(testProperties.getPhotosetId());
        _log.debug(photoset.getUrl());
        assertNotNull(photoset);
        assertNotNull(photoset.getPrimaryPhoto());
        assertTrue(photoset.getPhotoCount() >= 1);
    }

    @Test
    public void testGetList() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList(testProperties.getNsid());
        assertNotNull(photosets);
        assertFalse(photosets.getPhotosets().isEmpty());
    }

    @Test
    public void testGetListWithExtras() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList(testProperties.getNsid(), "last_update, owner_name");
        assertNotNull(photosets);
        Collection<Photoset> photosetsList = photosets.getPhotosets();
        assertFalse(photosetsList.isEmpty());
        Photoset photoset = photosetsList.iterator().next();
        assertNotNull(photoset.getPrimaryPhoto().getLastUpdate());
        assertNotNull(photoset.getPrimaryPhoto().getOwner());
        assertNotNull(photoset.getPrimaryPhoto().getOwner().getUsername());
        assertTrue(photoset.getPrimaryPhoto().getOwner().getUsername().length() > 0);
    }

    @Test
    public void testGetPhotos() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoList<Photo> photos = iface.getPhotos(testProperties.getPhotosetId(), 10, 1);
        assertNotNull(photos);
        assertTrue(photos.size() >= 1);
        assertNotNull(photos.get(0).getOwner().getUsername());
        assertNotNull(photos.get(0).getOwner().getId());
    }

    @Test
    public void testOrderSets() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        String[] photosetIds = { testProperties.getPhotosetId() };
        iface.orderSets(photosetIds);
    }

    /*
     * @Test public void testRemovePhoto() throws FlickrException { PhotosetsInterface iface = flickr.getPhotosetsInterface(); String[] photosetIds =
     * {testProperties.getPhotoSetId()}; iface.orderSets(photosetIds); }
     */
    @Test
    public void testRemovePhotos() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.create("test", "A test photoset", testProperties.getPhotoId());

        PhotoList<Photo> photos = iface.getPhotos(photoset.getId(), 10, 1);
        assertNotNull(photos);

        iface.removePhoto(photoset.getId(), testProperties.getPhotoId());

        try {
            photos = iface.getPhotos(photoset.getId(), 10, 1);
        } catch (FlickrException e) {
            // photoset should be nuked when the only photo is removed from it
        }
    }
}
