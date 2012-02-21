/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.blogs.Blog;
import com.flickr4java.flickr.blogs.BlogsInterface;
import com.flickr4java.flickr.blogs.Service;

/**
 * @author Anthony Eden
 */
public class BlogsInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        testProperties = new TestProperties();
        REST rest = new REST();

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testGetList() throws FlickrException, IOException, SAXException {
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection<Blog> blogs = blogsInterface.getList();
        assertNotNull(blogs);
        assertEquals(0, blogs.size());
    }

    @Test
    public void testGetServices() throws FlickrException, IOException, SAXException {
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection<Service> services = blogsInterface.getServices();
        Iterator<Service> it = services.iterator();
        boolean bloggerFound = false;
        while (it.hasNext()) {
            Service ser = (Service) it.next();
            if (ser.getId().equals("beta.blogger.com") && ser.getName().equals("Blogger")) {
                bloggerFound = true;
            }
            // System.out.println(ser.getId() + " " + ser.getName());
        }
        assertTrue(bloggerFound);
    }

    @Test
    public void testPostImage() {
        // TODO: implement this test
    }

}
