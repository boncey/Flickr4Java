/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.contacts.Contact;
import com.flickr4java.flickr.contacts.ContactsInterface;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Anthony Eden
 * @version $Id: ContactsInterfaceTest.java,v 1.9 2009/01/01 20:25:57 x-mago Exp $
 */
public class ContactsInterfaceTest {

    Flickr flickr = null;
    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        testProperties = new TestProperties();

        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(testProperties.getApiKey())
                .apiSecret(testProperties.getSecret()).build();
        REST rest = new REST();

        flickr = new Flickr(
                testProperties.getApiKey(),
                testProperties.getSecret(),
                rest
                );

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
        ContactsInterface iface = flickr.getContactsInterface();
        Collection contacts = iface.getList();
        assertNotNull(contacts);
        assertTrue("No Contacts. (You need to have contacts for this test to succceed)", contacts.size() > 0);
        Iterator it = contacts.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            Contact contact = (Contact)it.next();
            assertNotNull(contact.getUsername());
            assertNotNull(contact.getRealName());
            assertNotNull(contact.getId());
            assertTrue(contact.getIconFarm() > 0);
            assertTrue(contact.getIconServer() > 0);
        }
    }

    @Test
    public void testGetPublicList() throws FlickrException, IOException, SAXException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection contacts = iface.getPublicList(testProperties.getNsid());
        assertNotNull(contacts);
        assertTrue("No Contacts. (You need to have contacts for this test to succceed)", contacts.size() > 0);
        Iterator it = contacts.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            Contact contact = (Contact)it.next();
            assertNotNull(contact.getUsername());
            assertNotNull(contact.getId());
            assertTrue(contact.getIconFarm() > 0);
            assertTrue(contact.getIconServer() > 0);
        }
    }

}
