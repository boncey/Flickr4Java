package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.tags.TagsInterface;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.tags.RelatedTagsList;
import com.flickr4java.flickr.photos.Photo;
import org.xml.sax.SAXException;
import junit.framework.TestCase;

/**
 * @author Anthony Eden
 */
public class TagsInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            SOAP soap = new SOAP(properties.getProperty("host"));
            flickr = new Flickr(properties.getProperty("apiKey"), soap);

        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetListPhoto() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Photo photo = iface.getListPhoto(properties.getProperty("photoid"));
        assertNotNull(photo);
        assertEquals(properties.getProperty("photoid"), photo.getId());
        assertNotNull(photo.getTags());
        assertEquals(0, photo.getTags().size());
    }

    public void testGetListUser() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getListUser(properties.getProperty("nsid"));
        assertNotNull(tags);
        assertEquals(1, tags.size());
    }

    public void testListUserPopular() throws FlickrException, IOException, SAXException {
        TagsInterface iface = flickr.getTagsInterface();
        Collection tags = iface.getListUserPopular(properties.getProperty("nsid"));
        assertNotNull(tags);
        assertEquals(1, tags.size());
        Iterator iter = tags.iterator();
        while (iter.hasNext()) {
            Tag tag = (Tag) iter.next();
            assertNotNull(tag.getValue());
            System.out.println(tag.getValue() + ":" + tag.getCount());
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
