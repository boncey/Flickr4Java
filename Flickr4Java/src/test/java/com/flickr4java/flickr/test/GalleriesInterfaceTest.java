/**
 * @author acaplan
 */
package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.galleries.GalleriesInterface;
import com.flickr4java.flickr.galleries.Gallery;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * @author acaplan
 * 
 */
public class GalleriesInterfaceTest extends Flickr4JavaTest {

    @After
    public void tearDown() {
        flickr = null;
    }

    @Test
    public void testGetList() throws FlickrException {
        GalleriesInterface iface = flickr.getGalleriesInterface();
        List<Gallery> galleries = iface.getList(testProperties.getNsid(), 10, 1);
        assertNotNull(galleries);
        assertFalse(galleries.isEmpty());
    }

    @Ignore
    @Test
    public void testCreate() throws FlickrException {
        GalleriesInterface iface = flickr.getGalleriesInterface();
        Gallery gallery = iface.create("test_gallery", "test gallery", "2732893596");
        assertNotNull(gallery);
        assertNotNull(gallery.getId());
        assertEquals("test_gallery", gallery.getTitle());
        assertEquals("test gallery", gallery.getDesc());
    }

    @Test
    public void testGetInfo() throws FlickrException {
        GalleriesInterface iface = flickr.getGalleriesInterface();
        Gallery gallery = iface.getInfo("1979953-72157629277637049");
        assertNotNull(gallery);
        assertEquals("Motorcycle Pics", gallery.getTitle());
    }
}
