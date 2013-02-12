package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.collections.Collection;
import com.flickr4java.flickr.collections.CollectionsInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photosets.Photoset;

import org.junit.Test;

import java.util.List;

/**
 * @author Darren Greaves
 */
public class CollectionsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetInfo() throws FlickrException {
        CollectionsInterface iface = flickr.getCollectionsInterface();
        Collection collection = iface.getInfo(testProperties.getCollectionId());
        assertNotNull(collection);
        assertNotNull(collection.getId());
        assertNotNull(collection.getIconLarge());
        assertNotNull(collection.getIconSmall());
        assertNotNull(collection.getDateCreated());
        assertNotNull(collection.getDescription());
        assertNotNull(collection.getTitle());
        assertNotNull(collection.getServer());
        assertNotNull(collection.getSecret());

        List<Photo> photos = collection.getPhotos();
        assertNotNull(photos);
        for (Photo photo : photos) {
            assertNotNull(photo.getId());
        }
        assertTrue(collection.getChildCount() >= 1);
    }

    @Test
    public void testGetTreeNoUserId() throws FlickrException {
        CollectionsInterface iface = flickr.getCollectionsInterface();
        List<Collection> collections = iface.getTree(testProperties.getCollectionId(), null);

        assertNotNull(collections);

        for (Collection collection : collections) {
            assertNotNull(collection);
            assertNotNull(collection.getId());
            assertNotNull(collection.getIconLarge());
            assertNotNull(collection.getIconSmall());
            assertNotNull(collection.getDescription());
            assertNotNull(collection.getTitle());
            assertNull(collection.getServer());
            assertNull(collection.getSecret());

            List<Photo> photos = collection.getPhotos();
            assertNotNull(photos);
            assertTrue(photos.isEmpty());

            List<Photoset> photosets = collection.getPhotosets();
            assertNotNull(photosets);
            for (Photoset photoset : photosets) {
                assertNotNull(photoset.getId());
                assertNotNull(photoset.getTitle());
                assertNotNull(photoset.getDescription());
            }

            List<Collection> childCollections = collection.getCollections();
            for (Collection childCollection : childCollections) {
                assertNotNull(childCollection.getId());
                assertNotNull(childCollection.getIconLarge());
                assertNotNull(childCollection.getIconSmall());
                assertNotNull(childCollection.getDescription());
                assertNotNull(childCollection.getTitle());
            }
        }
    }

}
