

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.pools.PoolsInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;

import org.junit.Test;

import java.util.Collection;

/**
 * @author Anthony Eden
 */
public class PoolsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testAddAndRemove() throws FlickrException {
        PoolsInterface iface = flickr.getPoolsInterface();
        String photoId = testProperties.getPhotoId();
        String groupId = testProperties.getTestGroupId();

        try {
            iface.add(photoId, groupId);
        } finally {
            iface.remove(photoId, groupId);
        }
    }

    @Test
    public void testGetGroups() throws FlickrException {
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection<Group> groups = iface.getGroups();
        assertNotNull(groups);
        assertTrue(groups.size() >= 1);
    }

    @Test
    public void testGetPhotos() throws FlickrException {
        String groupId = testProperties.getTestGroupId();
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection<Photo> photos = iface.getPhotos(groupId, null, 0, 0);
        assertNotNull(photos);
        assertEquals(0, photos.size());
    }

    @Test
    public void testGetContext() throws FlickrException {
        String groupId = testProperties.getTestGroupId();
        String photoId = testProperties.getPhotoId();
        PoolsInterface iface = flickr.getPoolsInterface();

        try {
            iface.add(photoId, groupId);
            PhotoContext photoContext = iface.getContext(photoId, groupId);
            assertNotNull(photoContext);
        } finally {
            iface.remove(photoId, groupId);
        }
    }
}
