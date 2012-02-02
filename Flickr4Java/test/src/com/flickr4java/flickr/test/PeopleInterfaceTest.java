/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.PhotoList;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Anthony Eden
 * @version $Id: PeopleInterfaceTest.java,v 1.15 2010/07/12 19:11:30 x-mago Exp $
 */
public class PeopleInterfaceTest {

    Flickr flickr = null;
    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        //Flickr.debugStream = true;

        testProperties = new TestProperties();

        REST rest = new REST();
        rest.setHost(testProperties.getHost());

        flickr = new Flickr(
                testProperties.getApiKey(),
                testProperties.getSecret(),
                rest
                );

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testFindByEmail() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByEmail(testProperties.getEmail());
        assertNotNull(person);
        assertEquals(person.getId(), testProperties.getNsid());
        assertEquals(person.getUsername(), testProperties.getUsername());
    }

    @Test
    public void testFindByUsername() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByUsername(testProperties.getUsername());
        assertNotNull(person);
        assertEquals(testProperties.getNsid(), person.getId());
        assertEquals(testProperties.getUsername(), person.getUsername());
        // Do the UrlEcoding is correct?
        person = iface.findByUsername("K H A L E D");
        assertNotNull(person);
        assertEquals("7478210@N02", person.getId());
        assertEquals("K H A L E D", person.getUsername());
    }

    @Test
    public void testGetInfo() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.getInfo(testProperties.getNsid());
        assertNotNull(person);
        assertEquals(testProperties.getNsid(), person.getId());
        assertEquals(testProperties.getUsername(), person.getUsername());
        assertTrue(person.getMobileurl().startsWith("http://m.flickr.com/photostream.gne"));
        assertEquals(person.getPhotosurl(), String.format("http://www.flickr.com/photos/%s/", testProperties.getUsername()));
        assertEquals(person.getProfileurl(), String.format("http://www.flickr.com/people/%s/", testProperties.getUsername()));
        assertTrue(person.getBuddyIconUrl().startsWith("http://"));
    }

    @Test
    public void testGetPublicGroups() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        Collection groups = iface.getPublicGroups(testProperties.getNsid());
        assertNotNull(groups);
        assertTrue(groups.size() >= 1);
    }

    @Test
    public void testGetPublicPhotos() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PhotoList photos = iface.getPublicPhotos(testProperties.getNsid(), 0, 0);
        assertNotNull(photos);
        assertTrue(photos.size() >= 1);
    }

    @Test
    public void testGetUploadStatus() {

    }

}
