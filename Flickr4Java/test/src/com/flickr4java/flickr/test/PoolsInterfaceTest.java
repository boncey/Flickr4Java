/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.pools.PoolsInterface;
import com.flickr4java.flickr.photos.Photo;

/**
 * @author Anthony Eden
 */
public class PoolsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testAddAndRemove() throws FlickrException {
        String photoId = testProperties.getPhotoId();
        String groupId = testProperties.getTestGroupId();
        PoolsInterface iface = flickr.getPoolsInterface();
        iface.add(photoId, groupId);
        iface.remove(photoId, groupId);
    }

    @Test
    public void testGetContext() {

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

}
