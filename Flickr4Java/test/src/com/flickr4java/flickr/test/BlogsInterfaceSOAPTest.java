/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.blogs.BlogsInterface;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Matt Ray
 */
public class BlogsInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            
            SOAP soap = new SOAP(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), soap);
            
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetList() throws FlickrException, IOException, SAXException {
        RequestContext requestContext = RequestContext.getRequestContext();
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection blogs = blogsInterface.getList();
        assertNotNull(blogs);
        assertEquals(1, blogs.size());
    }

    public void testPostImage() {
        RequestContext requestContext = RequestContext.getRequestContext();
    }

}
