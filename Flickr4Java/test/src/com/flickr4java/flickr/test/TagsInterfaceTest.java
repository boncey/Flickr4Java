package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.tags.Cluster;
import com.flickr4java.flickr.tags.ClusterList;
import com.flickr4java.flickr.tags.RelatedTagsList;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.tags.TagsInterface;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class TagsInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugStream = false;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(
                properties.getProperty("apiKey"),
                properties.getProperty("secret"),
                rest
            );

            RequestContext requestContext = RequestContext.getRequestContext();

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetClusters() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        ClusterList clusters = iface.getClusters("api");
        //System.out.println("size " + clusters.getClusters().get(2).getTags().size());
        assertTrue(clusters.getClusters().size() >= 3);
        Cluster cluster = clusters.getClusters().get(0);
        assertTrue(cluster.getTags().size() >= 1);
        cluster = clusters.getClusters().get(1);
        assertTrue(cluster.getTags().size() >= 1);
        cluster = clusters.getClusters().get(2);
        assertTrue(cluster.getTags().size() >= 1);
    }

    public void testGetClusterPhotos() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        PhotoList photos = iface.getClusterPhotos("ducati", "999-1098-848");
        assertTrue(photos.getTotal() == 24);
    }

    public void testGetListPhoto() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Photo photo = iface.getListPhoto(properties.getProperty("photoid"));
        assertNotNull(photo);
        assertEquals(properties.getProperty("photoid"), photo.getId());
        assertNotNull(photo.getTags());
        assertEquals(3, photo.getTags().size());
    }

    public void testGetHotList() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getHotList("day", 20);
        assertNotNull(tags);
        assertTrue(tags.size() > 1);
    }

    public void testGetListUser() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getListUser(properties.getProperty("nsid"));
        assertNotNull(tags);
        assertEquals(4, tags.size());
    }

    public void testListUserPopular() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getListUserPopular(properties.getProperty("nsid"));
        assertNotNull(tags);
        assertEquals(4, tags.size());
        Iterator iter = tags.iterator();
        while (iter.hasNext()) {
            Tag tag = (Tag) iter.next();
            assertNotNull(tag.getValue());
            //System.out.println(tag.getValue() + ":" + tag.getCount());
        }
    }

    public void testGetRelated() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        RelatedTagsList relatedTags = iface.getRelated("flower");
        assertNotNull(relatedTags);
        assertEquals("flower", relatedTags.getSource());
        assertTrue("Number of related tags returned was 0", relatedTags.size() > 0);
    }

}
