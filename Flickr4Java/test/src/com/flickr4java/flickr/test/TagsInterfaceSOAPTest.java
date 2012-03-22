package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.tags.RelatedTagsList;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.tags.TagsInterface;

/**
 * @author Anthony Eden
 */
public class TagsInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        SOAP soap = new SOAP(testProperties.getHost());
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

    }

    @Ignore
    @Test
    public void testGetListPhoto() throws FlickrException {
        TagsInterface iface = flickr.getTagsInterface();
        Photo photo = iface.getListPhoto(testProperties.getPhotoId());
        assertNotNull(photo);
        assertEquals(testProperties.getPhotoId(), photo.getId());
        assertNotNull(photo.getTags());
        assertEquals(3, photo.getTags().size());
    }

    @Ignore
    @Test
    public void testGetListUser() throws FlickrException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection<Tag> tags = iface.getListUser(testProperties.getNsid());
        assertNotNull(tags);
        assertEquals(756, tags.size());
    }

    @Ignore
    @Test
    public void testListUserPopular() throws FlickrException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection<Tag> tags = iface.getListUserPopular(testProperties.getNsid());
        assertNotNull(tags);
        assertEquals(10, tags.size());
        for (Tag tag : tags) {
            assertNotNull(tag.getValue());
            System.out.println(tag.getValue() + ":" + tag.getCount());
        }
    }

    @Ignore
    @Test
    public void testGetRelated() throws FlickrException {
        TagsInterface iface = flickr.getTagsInterface();
        RelatedTagsList relatedTags = iface.getRelated("flower");
        assertNotNull(relatedTags);
        assertEquals("flower", relatedTags.getSource());
        assertTrue("Number of related tags returned was 0", relatedTags.size() > 0);
    }

}
