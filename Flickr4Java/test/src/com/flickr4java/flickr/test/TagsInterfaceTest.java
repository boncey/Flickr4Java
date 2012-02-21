package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.tags.Cluster;
import com.flickr4java.flickr.tags.ClusterList;
import com.flickr4java.flickr.tags.RelatedTagsList;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.tags.TagsInterface;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Anthony Eden
 */
public class TagsInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugStream = false;
        testProperties = new TestProperties();

        REST rest = new REST();
        rest.setHost(testProperties.getHost());

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testGetClusters() throws FlickrException, IOException, SAXException {
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
    public void testGetClusterPhotos() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        PhotoList photos = iface.getClusterPhotos("ducati", "999-1098-848");
        assertTrue(photos.getTotal() == 24);
    }

    @Test
    public void testGetListPhoto() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Photo photo = iface.getListPhoto(testProperties.getPhotoId());
        assertNotNull(photo);
        assertEquals(testProperties.getPhotoId(), photo.getId());
        assertNotNull(photo.getTags());
        assertFalse(photo.getTags().isEmpty());
    }

    @Test
    public void testGetHotList() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getHotList("day", 20);
        assertNotNull(tags);
        assertTrue(tags.size() > 1);
    }

    @Test
    public void testGetListUser() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getListUser(testProperties.getNsid());
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void testListUserPopular() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getListUserPopular(testProperties.getNsid());
        assertNotNull(tags);
        assertEquals(10, tags.size());
        Iterator iter = tags.iterator();
        while (iter.hasNext()) {
            Tag tag = (Tag) iter.next();
            assertNotNull(tag.getValue());
            // System.out.println(tag.getValue() + ":" + tag.getCount());
        }
    }

    @Test
    public void testGetRelated() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        RelatedTagsList relatedTags = iface.getRelated("flower");
        assertNotNull(relatedTags);
        assertEquals("flower", relatedTags.getSource());
        assertTrue("Number of related tags returned was 0", relatedTags.size() > 0);
    }

}
