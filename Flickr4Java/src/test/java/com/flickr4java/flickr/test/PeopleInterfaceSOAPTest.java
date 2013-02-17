/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;

/**
 * @author Anthony Eden
 */
public class PeopleInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, FlickrException {
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
    public void testFindByEmail() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByEmail(testProperties.getEmail());
        assertNotNull(person);
        assertEquals(person.getId(), testProperties.getNsid());
        assertEquals(person.getUsername(), testProperties.getUsername());
    }

    @Ignore
    @Test
    public void testFindByUsername() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByUsername(testProperties.getUsername());
        assertNotNull(person);
        assertEquals(testProperties.getNsid(), person.getId());
        assertEquals(testProperties.getUsername(), person.getUsername());
    }

    @Ignore
    @Test
    public void testGetInfo() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.getInfo(testProperties.getNsid());
        assertNotNull(person);
        assertEquals(testProperties.getNsid(), person.getId());
        assertEquals(testProperties.getUsername(), person.getUsername());
    }

    @Ignore
    @Test
    public void testGetPublicGroups() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        Collection<Group> groups = iface.getPublicGroups(testProperties.getNsid());
        assertNotNull(groups);
        assertEquals(229, groups.size());
    }

    @Ignore
    @Test
    public void testGetPublicPhotos() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        Collection<Photo> photos = iface.getPublicPhotos(testProperties.getNsid(), 0, 0);
        assertNotNull(photos);
        assertEquals(100, photos.size());
    }

    @Ignore
    @Test
    public void testGetUploadStatus() {

    }

}
