package com.flickr4java.flickr.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.contacts.OnlineStatus;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Editability;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Note;
import com.flickr4java.flickr.photos.Permissions;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.tags.Tag;

import org.junit.Test;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author mago
 * @version $Id: ObjectTest.java,v 1.1 2009/07/23 20:41:03 x-mago Exp $
 */
public class ObjectTest {
    /**
     * Testing the equals-implementation.
     * 
     * Don't test every single member, as they are covered in equals() by reflection. Emphasis on the child-objects.
     */
    @Test
    public void testPhoto() {
        Photo p1 = new Photo();
        Photo p2 = new Photo();
        assertTrue(p1.equals(p2));

        p1.setSecret("secret");
        assertFalse(p1.equals(p2));
        p2.setSecret("secret");
        assertTrue(p1.equals(p2));

        p1.setComments(100);
        assertFalse(p1.equals(p2));
        p2.setComments(100);
        assertTrue(p1.equals(p2));

        Editability edit1 = new Editability();
        edit1.setAddmeta(true);
        edit1.setComment(true);
        Editability edit2 = new Editability();
        edit2.setAddmeta(true);
        edit2.setComment(true);
        p1.setEditability(edit1);
        assertFalse(p1.equals(p2));
        p2.setEditability(edit2);
        assertTrue(p1.equals(p2));
        edit2.setComment(false);
        p2.setEditability(edit2);
        assertFalse(p1.equals(p2));
        edit2.setComment(true);

        GeoData geo1 = new GeoData();
        geo1.setAccuracy(1);
        geo1.setLatitude(1.2F);
        geo1.setLongitude(1.1F);
        GeoData geo2 = new GeoData();
        geo2.setAccuracy(1);
        geo2.setLatitude(1.2F);
        geo2.setLongitude(1.1F);
        p1.setGeoData(geo1);
        assertFalse(p1.equals(p2));
        p2.setGeoData(geo2);
        assertTrue(p1.equals(p2));
        geo2.setAccuracy(2);
        p2.setGeoData(geo2);
        assertFalse(p1.equals(p2));
        geo2.setAccuracy(1);

        Size size1 = new Size();
        size1.setLabel(Size.THUMB);
        size1.setHeight(100);
        size1.setWidth(100);
        size1.setSource("url");
        Size size2 = new Size();
        size2.setLabel(Size.SMALL);
        size2.setHeight(100);
        size2.setWidth(100);
        size2.setSource("url");
        ArrayList<Size> sizes = new ArrayList<Size>();
        sizes.add(size1);
        sizes.add(size2);
        p1.setSizes(sizes);
        assertFalse(p1.equals(p2));
        p2.setSizes(sizes);
        assertTrue(p1.equals(p2));

        Permissions perm1 = new Permissions();
        perm1.setId("id");
        perm1.setAddmeta(1);
        perm1.setFamilyFlag(true);
        perm1.setFriendFlag(true);
        perm1.setPublicFlag(true);
        Permissions perm2 = new Permissions();
        perm2.setId("id");
        perm2.setAddmeta(1);
        perm2.setFamilyFlag(true);
        perm2.setFriendFlag(true);
        perm2.setPublicFlag(true);
        p1.setPermissions(perm1);
        assertFalse(p1.equals(p2));
        p2.setPermissions(perm2);
        assertTrue(p1.equals(p2));
        perm2.setAddmeta(2);
        p2.setPermissions(perm2);
        assertFalse(p1.equals(p2));
        perm2.setAddmeta(1);

        User user1 = new User();
        user1.setId("id");
        user1.setAdmin(true);
        user1.setOnline(OnlineStatus.OFFLINE);
        user1.setIconFarm(1);
        user1.setIconServer(1);
        User user2 = new User();
        user2.setId("id");
        user2.setAdmin(true);
        user2.setOnline(OnlineStatus.OFFLINE);
        user2.setIconFarm(1);
        user2.setIconServer(1);

        p1.setOwner(user1);
        assertFalse(p1.equals(p2));
        p2.setOwner(user2);
        assertTrue(p1.equals(p2));
        user2.setOnline(OnlineStatus.ONLINE);
        p2.setOwner(user2);
        assertFalse(p1.equals(p2));
        user2.setOnline(OnlineStatus.OFFLINE);

        Note note1 = new Note();
        Note note2 = new Note();
        Note note3 = new Note();
        note1.setId("id");
        note1.setAuthor("author");
        note1.setAuthorName("author");
        note1.setBounds(new Rectangle(0, 1, 2, 3));
        note2.setId("id");
        note2.setAuthor("author");
        note2.setAuthorName("author");
        note2.setBounds(new Rectangle(0, 1, 2, 3));
        note3.setId("id");
        note3.setAuthor("author");
        note3.setAuthorName("author");
        note3.setBounds(new Rectangle(0, 1, 2, 3));
        List<Note> notes1 = new ArrayList<Note>();
        notes1.add(note1);
        notes1.add(note2);
        List<Note> notes2 = new ArrayList<Note>();
        notes2.add(note1);
        notes2.add(note3);
        p1.setNotes(notes1);
        assertFalse(p1.equals(p2));
        p2.setNotes(notes2);
        assertTrue(p1.equals(p2));

        List<Tag> tags1 = new ArrayList<Tag>();
        List<Tag> tags2 = new ArrayList<Tag>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        Tag tag3 = new Tag();
        tag1.setAuthor("author");
        tag1.setAuthorName("authorName");
        tag1.setCount(1);
        tag1.setId("id");
        tag1.setRaw("raw");
        tag1.setValue("value");
        tag2.setAuthor("author");
        tag2.setAuthorName("authorName");
        tag2.setCount(1);
        tag2.setId("id");
        tag2.setRaw("raw");
        tag2.setValue("value");
        tag3.setAuthor("author");
        tag3.setAuthorName("authorName");
        tag3.setCount(1);
        tag3.setId("id");
        tag3.setRaw("raw");
        tag3.setValue("value");
        tags1.add(tag1);
        tags1.add(tag2);
        tags2.add(tag1);
        tags2.add(tag3);
        p1.setTags(tags1);
        assertFalse(p1.equals(p2));
        p2.setTags(tags2);
        assertTrue(p1.equals(p2));

        Calendar cal = Calendar.getInstance();
        p1.setDateTaken(cal.getTime());
        assertFalse(p1.equals(p2));
        p2.setDateTaken(cal.getTime());
        assertTrue(p1.equals(p2));
        p2.setDateTaken(new Date());
        assertFalse(p1.equals(p2));
        p2.setDateTaken(cal.getTime());
        assertTrue(p1.equals(p2));
    }
}
