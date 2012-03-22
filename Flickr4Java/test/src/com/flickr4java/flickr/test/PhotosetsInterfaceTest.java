package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

/**
 * @author Anthony Eden
 */
public class PhotosetsInterfaceTest extends Flickr4JavaTest {

    private Photoset testSet = null;

    private String[] setPics = new String[] { "6883063595", "4271899426", "4263038141" };

    private String[] reordered = new String[] { "", "4263038141", "4271899426", "6883063595" };

    @Before
    public void setUp() throws FlickrException {
        super.setUp();
        // @TODO need to get this into the test.properties
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        testSet = iface.create("PhotosetsInterfaceTest", "JUnit test, should be deleted", setPics[0]);
        iface.addPhoto(testSet.getId(), setPics[1]);
        iface.addPhoto(testSet.getId(), setPics[2]);
        reordered[0] = testProperties.getPhotosetId();
    }

    @After
    public void tearDown() throws FlickrException {
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
        iface.addPhoto(testSet.getId(), testProperties.getPhotoId());
        iface.editPhotos(testSet.getId(), testProperties.getPhotoId(), reordered);

        Photoset ps = iface.getInfo(testSet.getId());
        assertNotNull(ps);
        assertEquals(testProperties.getPhotoId(), ps.getPrimaryPhoto());
        assertEquals(4, ps.getPhotoCount());
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
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photoset photoset = iface.getInfo(testProperties.getPhotosetId());
        System.err.println(photoset.getUrl());
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
    public void testGetList2() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        Photosets photosets = iface.getList("26095690@N00");
        assertNotNull(photosets);
    }

    @Test
    public void testGetPhotos() throws FlickrException {
        PhotosetsInterface iface = flickr.getPhotosetsInterface();
        PhotoList<Photo> photos = iface.getPhotos(testProperties.getPhotosetId(), 10, 1);
        assertNotNull(photos);
        assertTrue(photos.size() >= 1);
        assertEquals(testProperties.getUsername(), ((Photo) photos.get(0)).getOwner().getUsername());
        assertEquals(testProperties.getNsid(), ((Photo) photos.get(0)).getOwner().getId());
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
        assertEquals(1, photos.size());

        iface.removePhoto(photoset.getId(), testProperties.getPhotoId());

        try {
            photos = iface.getPhotos(photoset.getId(), 10, 1);
            fail("Photoset shouldn't still exist");
        } catch (FlickrException e) {
            // photoset should be nuked when the only photo is removed from it
        }
    }
}
