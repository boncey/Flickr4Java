/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.flickr4java.flickr.groups.Category;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupsInterface;
import com.flickr4java.flickr.groups.Subcategory;

/**
 * @author Anthony Eden
 */
public class GroupsInterfaceSOAPTest {

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
    public void testBrowse() throws FlickrException {
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

    @Ignore
    @Test
    public void testBrowseWithId() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Category cat = iface.browse("68"); // browse the Flickr category
        assertNotNull(cat);
        assertEquals("Flickr", cat.getName());

        Collection<Group> groups = cat.getGroups();
        assertNotNull(groups);
        assertTrue(groups.size() > 0);

        Collection<Subcategory> subcats = cat.getSubcategories();
        assertNotNull(subcats);
        assertTrue(subcats.size() > 0);
        // System.out.println("category name: " + cat.getName());
    }

    @Ignore
    @Test
    public void testGetInfo() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Group group = iface.getInfo("34427469792@N01");
        assertNotNull(group);
        assertEquals("34427469792@N01", group.getId());
        assertEquals("FlickrCentral", group.getName());
        System.out.println("group members: " + group.getMembers());
    }

    @Ignore
    @Test
    public void testSearch() throws FlickrException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Collection<Group> groups = iface.search("java", -1, -1);
        System.out.println("Group count: " + groups.size());
        assertTrue(groups.size() > 0);
    }

}
