/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.Category;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.groups.GroupsInterface;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class GroupsInterfaceTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;

        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
            properties.load(in);

OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
    				.apiSecret(properties.getProperty("secret")).build();
            REST rest = new REST(service);
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(
                properties.getProperty("apiKey"),
                properties.getProperty("secret"),
                rest
            );

			Auth auth = new Auth();
			auth.setPermission(Permission.READ);
			auth.setToken(properties.getProperty("token"));
			auth.setTokenSecret(properties.getProperty("tokensecret"));

			RequestContext requestContext = RequestContext.getRequestContext();
			requestContext.setAuth(auth);
			flickr.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void deprecatedBrowse() throws FlickrException, IOException, SAXException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Category cat = iface.browse(null);
        assertNotNull(cat);
        assertEquals("/", cat.getName());
//        System.out.println("category path: " + cat.getPath());

        Collection groups = cat.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.size());
//        Iterator groupsIter = groups.iterator();
//        while (groupsIter.hasNext()) {
//            Group group = (Group) groupsIter.next();
//            System.out.println("group id: " + group.getId());
//            System.out.println("group name: " + group.getName());
//        }

        Collection subcats = cat.getSubcategories();
        assertNotNull(subcats);
        assertTrue(subcats.size() > 0);
//        Iterator subcatsIter = subcats.iterator();
//        while (subcatsIter.hasNext()) {
//            Subcategory subcategory = (Subcategory) subcatsIter.next();
//            System.out.println("subcat id: " + subcategory.getId());
//            System.out.println("subcat name: " + subcategory.getName());
//        }
    }

    /*
     * It is not longer possible to browse the groups hierarchy
     *
    public void testBrowseWithId() throws FlickrException, IOException, SAXException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Category cat = iface.browse("68"); // browse the Flickr category
        assertNotNull(cat);
        assertEquals("Flickr", cat.getName());
        assertNotNull(cat.getPath());
        assertNotNull(cat.getPathIds());

        Collection groups = cat.getGroups();
        assertNotNull(groups);
        assertTrue(groups.size() > 0);

        Collection subcats = cat.getSubcategories();
        assertNotNull(subcats);
        assertTrue(subcats.size() > 0);
//        System.out.println("category name: " + cat.getName());
    } */

    public void testGetInfo() throws FlickrException, IOException, SAXException {
        GroupsInterface iface = flickr.getGroupsInterface();
        Group group = iface.getInfo("34427469792@N01");

        assertNotNull(group);
        assertEquals("34427469792@N01", group.getId());
        assertEquals("FlickrCentral", group.getName());
        assertTrue(group.getMembers() > 0);
        assertTrue(group.getBuddyIconUrl().startsWith("http://farm"));

        //System.out.println("group members: " + group.getMembers());
    }

    public void testSearch() throws FlickrException, IOException, SAXException {
        GroupsInterface iface = flickr.getGroupsInterface();
        GroupList groups = (GroupList) iface.search("java", 0, 0);
        assertTrue(groups.size() > 0);
        assertEquals(1, groups.getPage());
        assertTrue(groups.getPages() > 0);
        assertEquals(100, groups.getPerPage());
        assertTrue(groups.getTotal() > 0);
    }

    public void testSearchPage() throws FlickrException, IOException, SAXException {
        GroupsInterface iface = flickr.getGroupsInterface();
        GroupList groups = (GroupList) iface.search("java", 10, 1);
        assertTrue(groups.size() > 0);
        assertEquals(1, groups.getPage());
        assertTrue(groups.getPages() > 0);
        assertEquals(10, groups.getPerPage());
        assertTrue(groups.getTotal() > 0);
    }

}
