/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.groups.Category;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.groups.GroupsInterface;
import com.flickr4java.flickr.groups.Subcategory;

import org.junit.Test;

import java.util.Collection;

/**
 * @author Anthony Eden
 */
public class GroupsInterfaceTest extends Flickr4JavaTest {

    public static final String TEST_JOIN_GROUP = "2173840@N20";

    public void deprecatedBrowse() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Category cat = iface.browse(null);
        assertNotNull(cat);
        assertEquals("/", cat.getName());
        // System.out.println("category path: " + cat.getPath());

        Collection<Group> groups = cat.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.size());
        // Iterator groupsIter = groups.iterator();
        // while (groupsIter.hasNext()) {
        // Group group = (Group) groupsIter.next();
        // System.out.println("group id: " + group.getId());
        // System.out.println("group name: " + group.getName());
        // }

        Collection<Subcategory> subcats = cat.getSubcategories();
        assertNotNull(subcats);
        assertTrue(subcats.size() > 0);
        // Iterator subcatsIter = subcats.iterator();
        // while (subcatsIter.hasNext()) {
        // Subcategory subcategory = (Subcategory) subcatsIter.next();
        // System.out.println("subcat id: " + subcategory.getId());
        // System.out.println("subcat name: " + subcategory.getName());
        // }
    }

    /*
     * It is not longer possible to browse the groups hierarchy
     * 
     * @Test public void testBrowseWithId() throws FlickrException { GroupsInterface iface = flickr.getGroupsInterface(); Category cat = iface.browse("68"); //
     * browse the Flickr category assertNotNull(cat); assertEquals("Flickr", cat.getName()); assertNotNull(cat.getPath()); assertNotNull(cat.getPathIds());
     * 
     * Collection groups = cat.getGroups(); assertNotNull(groups); assertTrue(groups.size() > 0);
     * 
     * Collection subcats = cat.getSubcategories(); assertNotNull(subcats); assertTrue(subcats.size() > 0); // System.out.println("category name: " +
     * cat.getName()); }
     */

    @Test
    public void testGetInfo() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Group group = iface.getInfo("34427469792@N01");

        assertNotNull(group);
        assertEquals("34427469792@N01", group.getId());
        assertEquals("FlickrCentral", group.getName());
        assertTrue(group.getMembers() > 0);
        assertTrue(group.getSecureBuddyIconUrl().startsWith("https://farm"));

        // System.out.println("group members: " + group.getMembers());
    }

    @Test
    public void testSearch() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        GroupList<Group> groups = (GroupList<Group>) iface.search("java", 0, 0);
        assertTrue(groups.size() > 0);
        assertEquals(1, groups.getPage());
        assertTrue(groups.getPages() > 0);
        assertEquals(100, groups.getPerPage());
        assertTrue(groups.getTotal() > 0);
    }

    @Test
    public void testSearchPage() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        GroupList<Group> groups = (GroupList<Group>) iface.search("java", 10, 1);
        assertTrue(groups.size() > 0);
        assertEquals(1, groups.getPage());
        assertTrue(groups.getPages() > 0);
        assertEquals(10, groups.getPerPage());
        assertTrue(groups.getTotal() > 0);
    }

    @Test
    public void testJoinLeave() throws FlickrException {

        GroupsInterface iface = flickr.getGroupsInterface();

        Group group = iface.getInfo(TEST_JOIN_GROUP);
        int cntBeforeJoin = group.getMembers();
        iface.join(TEST_JOIN_GROUP, null);
        group = iface.getInfo(TEST_JOIN_GROUP);
        int cntAfterJoin = group.getMembers();
        assertTrue("Member count increased by 1", cntBeforeJoin + 1 == cntAfterJoin);

        iface.leave(TEST_JOIN_GROUP, false);
        group = iface.getInfo(TEST_JOIN_GROUP);
        int cntAfterLeave = group.getMembers();
        assertTrue("Member count decreased by 1", cntAfterLeave == cntBeforeJoin);
    }
}
