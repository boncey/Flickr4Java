/**
 * @author acaplan
 */
package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.galleries.GalleriesInterface;
import com.flickr4java.flickr.galleries.Gallery;

/**
 * @author acaplan
 * 
 */
public class GalleriesInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws FlickrException {
        testProperties = new TestProperties();
        REST rest = new REST();

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);
        Flickr.debugRequest = true;
        Flickr.debugStream = true;

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @After
    public void tearDown() {
        flickr = null;
    }

    @Test
    public void testGetList() throws FlickrException, IOException, SAXException {
        GalleriesInterface iface = flickr.getGalleriesInterface();
        List<Gallery> galleries = iface.getList(testProperties.getNsid(), 10, 1);
        assertNotNull(galleries);
        assertFalse(galleries.isEmpty());
    }

    /*
     * @Test public void testCreate() throws FlickrException, IOException, SAXException { GalleriesInterface iface = flickr.getGalleriesInterface(); Gallery
     * gallery = iface.create("test_gallery", "test gallery", "2732893596"); assertNotNull(gallery); assertNotNull(gallery.getId());
     * assertEquals("test_gallery", gallery.getTitle()); assertEquals("test gallery", gallery.getDesc()); }
     */

    @Test
    public void testGetInfo() throws FlickrException, IOException, SAXException {
        GalleriesInterface iface = flickr.getGalleriesInterface();
        Gallery gallery = iface.getInfo("1979953-72157629277637049");
        assertNotNull(gallery);
        assertEquals("Motorcycle Pics", gallery.getTitle());
    }
}
