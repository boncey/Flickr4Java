/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.people.UserList;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

/**
 * @author Anthony Eden
 * @version $Id: PeopleInterfaceTest.java,v 1.15 2010/07/12 19:11:30 x-mago Exp $
 */
public class PeopleInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testFindByEmail() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByEmail(testProperties.getEmail());
        assertNotNull(person);
        assertEquals(person.getId(), testProperties.getNsid());
        assertEquals(person.getUsername(), testProperties.getUsername());
    }

    @Test
    public void testFindByUsername() throws FlickrException {
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
    public void testGetInfo() throws FlickrException {
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
    public void testGetPublicGroups() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        Collection<Group> groups = iface.getPublicGroups(testProperties.getNsid());
        assertNotNull(groups);
        assertTrue(groups.size() >= 1);
    }

    @Test
    public void testGetPublicPhotos() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PhotoList<Photo> photos = iface.getPublicPhotos(testProperties.getNsid(), 0, 0);
        assertNotNull(photos);
        assertTrue(photos.size() >= 1);
    }

    @Test
    public void testGetUploadStatus() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User usr = iface.getUploadStatus();
        assertNotNull(usr);
        assertTrue(usr.getBandwidthMax() > 0);

    }

    @Test
    public void testGetPhotos() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PhotoList<Photo> photos = iface.getPhotos(testProperties.getNsid(), null, null, null, null, null, null, null, null, 15, 1);
        assertNotNull(photos);
        assertEquals(15, photos.size());
    }

    @Test
    public void testGetPhotosOf() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PhotoList<Photo> photos = iface.getPhotosOf(testProperties.getNsid(), null, null, 10, 1);
        assertNotNull(photos);
        assertEquals(10, photos.size());
    }

    @Test
    public void testAddDelete() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        iface.add(testProperties.getPhotoId(), testProperties.getNsid(), null);
        UserList<User> usrs = iface.getList(testProperties.getPhotoId());
        assertNotNull(usrs);
        assertEquals(1, usrs.size());
        iface.delete(testProperties.getPhotoId(), testProperties.getNsid());
        usrs = iface.getList(testProperties.getPhotoId());
        assertNotNull(usrs);
        assertEquals(0, usrs.size());
    }

    @Test
    public void testGetGroups() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        GroupList<Group> g = iface.getGroups();
        assertNotNull(g);
    }
}
