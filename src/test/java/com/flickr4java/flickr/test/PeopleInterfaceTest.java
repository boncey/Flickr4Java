

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.PersonTag;
import com.flickr4java.flickr.people.PersonTagList;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

import org.junit.Test;

import java.util.Collection;

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
        assertEquals(testProperties.getUsername(), person.getUsername());
        assertNotNull(person.getId());
    }

    @Test
    public void testGetInfo() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.getInfo(testProperties.getNsid());
        assertNotNull(person);
        assertEquals(testProperties.getNsid(), person.getId());
        assertEquals(testProperties.getDisplayname(), person.getRealName());
        assertTrue(person.getMobileurl().startsWith("https://m.flickr.com/photostream.gne"));
        assertEquals(person.getPhotosurl(), String.format("https://www.flickr.com/photos/%s/", testProperties.getUsername()));
        assertEquals(person.getProfileurl(), String.format("https://www.flickr.com/people/%s/", testProperties.getUsername()));
        assertTrue(person.getSecureBuddyIconUrl().startsWith("https://"));
        assertNotNull(person.getTimeZone());
        assertNotNull(person.getTimeZone().getTimeZoneId());
        assertNotNull(person.getTimeZone().getOffset());
        assertNotNull(person.getTimeZone().getLabel());
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
        assertTrue(photos.size() > 0);
    }

    @Test
    public void testGetPhotosOf() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PhotoList<Photo> photos = iface.getPhotosOf(testProperties.getNsid(), null, null, 10, 1);
        assertNotNull(photos);
        assertTrue(photos.size() > 0);
    }

    @Test
    public void testAddDelete() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PersonTagList<PersonTag> usrs = iface.getList(testProperties.getPhotoId());
        int size = usrs.size();
        try {
            iface.add(testProperties.getPhotoId(), testProperties.getNsid(), null);
        } finally {
            iface.delete(testProperties.getPhotoId(), testProperties.getNsid());
            usrs = iface.getList(testProperties.getPhotoId());
            assertNotNull(usrs);
            assertEquals(size, usrs.size());
        }
    }

    @Test
    public void testGetGroups() throws FlickrException {
        PeopleInterface iface = flickr.getPeopleInterface();
        GroupList<Group> g = iface.getGroups(testProperties.getNsid());
        assertNotNull(g);
    }
}
