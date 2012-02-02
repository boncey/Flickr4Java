/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.pools.PoolsInterface;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Anthony Eden
 */
public class PoolsInterfaceSOAPTest {

    Flickr flickr = null;
    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        SOAP soap = new SOAP(testProperties.getHost());
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Ignore
    @Test
    public void testAddAndRemove() throws FlickrException, IOException, SAXException {
        String photoId = testProperties.getPhotoId();
        String groupId = testProperties.getTestGroupId();
        PoolsInterface iface = flickr.getPoolsInterface();
        iface.add(photoId, groupId);
        iface.remove(photoId, groupId);
    }

    @Ignore
    @Test
    public void testGetContext() {

    }

    @Ignore
    @Test
    public void testGetGroups() throws FlickrException, IOException, SAXException {
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection groups = iface.getGroups();
        assertNotNull(groups);
        assertEquals(4, groups.size());
    }

    @Ignore
    @Test
    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        String groupId = testProperties.getTestGroupId();
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection photos = iface.getPhotos(groupId, null, 0, 0);
        assertNotNull(photos);
        assertEquals(4, photos.size());
    }

}
