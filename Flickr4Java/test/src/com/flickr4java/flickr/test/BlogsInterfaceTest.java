/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
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
import com.flickr4java.flickr.blogs.BlogsInterface;
import com.flickr4java.flickr.blogs.Service;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class BlogsInterfaceTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
            properties.load(in);

OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
    				.apiSecret(properties.getProperty("secret")).build();
            REST rest = new REST();

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

    public void testGetList() throws FlickrException, IOException, SAXException {
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection blogs = blogsInterface.getList();
        assertNotNull(blogs);
        assertEquals(0, blogs.size());
    }

    public void testGetServices() throws FlickrException, IOException, SAXException {
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection services = blogsInterface.getServices();
        Iterator it = services.iterator();
        boolean bloggerFound = false;
        while (it.hasNext()) {
            Service ser = (Service) it.next();
            if (ser.getId().equals("beta.blogger.com") &&
                ser.getName().equals("Blogger")) {
                bloggerFound = true;
            }
            //System.out.println(ser.getId() + " " + ser.getName());
        }
        assertTrue(bloggerFound);
    }

    public void testPostImage() {
        // TODO: implement this test
    }

}
