/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr;

import java.util.Properties;
import java.util.Collection;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.groups.pools.PoolsInterface;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Auth;
import org.xml.sax.SAXException;
import junit.framework.TestCase;

/**
 * @author Anthony Eden
 */
public class PoolsInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            SOAP soap = new SOAP(properties.getProperty("host"));
            flickr = new Flickr(properties.getProperty("apiKey"), soap);
            
            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setSharedSecret(properties.getProperty("secret"));

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testAddAndRemove() throws FlickrException, IOException, SAXException {
        String photoId = properties.getProperty("photoid");
        String groupId = properties.getProperty("testgroupid");
        PoolsInterface iface = flickr.getPoolsInterface();
        iface.add(photoId, groupId);
        iface.remove(photoId, groupId);
    }

    public void testGetContext() {

    }

    public void testGetGroups() throws FlickrException, IOException, SAXException {
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection groups = iface.getGroups();
        assertNotNull(groups);
        assertEquals(4, groups.size());
    }

    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        String groupId = properties.getProperty("testgroupid");
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection photos = iface.getPhotos(groupId, null, 0, 0);
        assertNotNull(photos);
        assertEquals(4, photos.size());
    }

}
