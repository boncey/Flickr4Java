package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.tags.Cluster;
import com.flickr4java.flickr.tags.ClusterList;
import com.flickr4java.flickr.tags.HotlistTag;
import com.flickr4java.flickr.tags.RelatedTagsList;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.tags.TagRaw;
import com.flickr4java.flickr.tags.TagsInterface;

import org.junit.Test;

import java.util.Collection;

/**
 * @author Anthony Eden
 */
public class TagsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetClusters() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        ClusterList clusters = iface.getClusters("api");
        // System.out.println("size " + clusters.getClusters().get(2).getTags().size());
        assertTrue(clusters.getClusters().size() >= 3);
        Cluster cluster = clusters.getClusters().get(0);
        assertTrue(cluster.getTags().size() >= 1);
        cluster = clusters.getClusters().get(1);
        assertTrue(cluster.getTags().size() >= 1);
        cluster = clusters.getClusters().get(2);
        assertTrue(cluster.getTags().size() >= 1);
    }

    @Test
    public void testGetClusterPhotos() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        PhotoList<Photo> photos = iface.getClusterPhotos("ducati", "999-1098-848");
        assertTrue(photos.getTotal() >= 20);
    }

    @Test
    public void testGetListPhoto() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        Photo photo = iface.getListPhoto(testProperties.getPhotoId());
        assertNotNull(photo);
        assertEquals(testProperties.getPhotoId(), photo.getId());
        assertNotNull(photo.getTags());
        assertFalse(photo.getTags().isEmpty());
    }

    @Test
    public void testGetHotList() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        Collection<HotlistTag> tags = iface.getHotList("day", 20);
        assertNotNull(tags);
        assertTrue(tags.size() > 1);
    }

    @Test
    public void testGetListUser() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        Collection<Tag> tags = iface.getListUser(testProperties.getNsid());
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void testGetListUserRaw() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        Collection<TagRaw> tags = iface.getListUserRaw();
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void testListUserPopular() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        Collection<Tag> tags = iface.getListUserPopular(testProperties.getNsid());
        assertNotNull(tags);
        assertEquals(10, tags.size());
        for (Tag tag : tags) {
            assertNotNull(tag.getValue());
        }
    }

    @Test
    public void testGetRelated() throws FlickrException {

        TagsInterface iface = flickr.getTagsInterface();
        RelatedTagsList relatedTags = iface.getRelated("flower");
        assertNotNull(relatedTags);
        assertEquals("flower", relatedTags.getSource());
        assertTrue("Number of related tags returned was 0", relatedTags.size() > 0);
    }

}
