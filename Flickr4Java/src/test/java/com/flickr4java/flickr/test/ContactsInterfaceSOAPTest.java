/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.flickr4java.flickr.contacts.Contact;
import com.flickr4java.flickr.contacts.ContactsInterface;

/**
 * @author Matt Ray
 */
public class ContactsInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, FlickrException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        SOAP soap = new SOAP();
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
    public void testGetList() throws FlickrException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection<Contact> contacts = iface.getList();
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
    }

    @Ignore
    @Test
    public void testGetPublicList() throws FlickrException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection<Contact> contacts = iface.getPublicList(testProperties.getNsid());
        assertNotNull(contacts);
        assertEquals(1, contacts.size());
    }

}
