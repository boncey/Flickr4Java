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
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 * @version $Id: PeopleInterfaceTest.java,v 1.15 2010/07/12 19:11:30 x-mago Exp $
 */
public class PeopleInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        //Flickr.debugStream = true;

        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
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

    public void testFindByEmail() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByEmail(properties.getProperty("email"));
        assertNotNull(person);
        assertEquals(person.getId(), properties.getProperty("nsid"));
        assertEquals(person.getUsername(), properties.getProperty("username"));
    }

    public void testFindByUsername() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.findByUsername(properties.getProperty("username"));
        assertNotNull(person);
        assertEquals(properties.getProperty("nsid"), person.getId());
        assertEquals(properties.getProperty("username"), person.getUsername());
        // Do the UrlEcoding is correct?
        person = iface.findByUsername("K H A L E D");
        assertNotNull(person);
        assertEquals("7478210@N02", person.getId());
        assertEquals("K H A L E D", person.getUsername());
    }

    public void testGetInfo() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        User person = iface.getInfo(properties.getProperty("nsid"));
        assertNotNull(person);
        assertEquals(properties.getProperty("nsid"), person.getId());
        assertEquals(properties.getProperty("username"), person.getUsername());
        assertEquals(person.getMobileurl(), "http://m.flickr.com/photostream.gne?id=1979953");
        assertEquals(person.getPhotosurl(), "http://www.flickr.com/photos/misteral/");
        assertEquals(person.getProfileurl(), "http://www.flickr.com/people/misteral/");
        assertTrue(person.getBuddyIconUrl().startsWith("http://"));
    }

    public void testGetPublicGroups() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        Collection groups = iface.getPublicGroups(properties.getProperty("nsid"));
        assertNotNull(groups);
        assertTrue(groups.size() == 1);
    }

    public void testGetPublicPhotos() throws FlickrException, IOException, SAXException {
        PeopleInterface iface = flickr.getPeopleInterface();
        PhotoList photos = iface.getPublicPhotos(properties.getProperty("nsid"), 0, 0);
        assertNotNull(photos);
        assertTrue(photos.size() > 3);
    }

    public void testGetUploadStatus() {

    }

}
