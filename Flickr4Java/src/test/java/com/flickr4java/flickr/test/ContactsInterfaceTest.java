/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.contacts.Contact;
import com.flickr4java.flickr.contacts.ContactsInterface;

/**
 * @author Anthony Eden
 * @version $Id: ContactsInterfaceTest.java,v 1.9 2009/01/01 20:25:57 x-mago Exp $
 */
public class ContactsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetList() throws FlickrException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection<Contact> contacts = iface.getList();
        assertNotNull(contacts);
        assertTrue("No Contacts. (You need to have contacts for this test to succceed)", contacts.size() > 0);
        Iterator<Contact> it = contacts.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            Contact contact = (Contact) it.next();
            assertNotNull(contact.getUsername());
            assertNotNull(contact.getRealName());
            assertNotNull(contact.getId());
            assertTrue(contact.getIconFarm() > 0);
            assertTrue(contact.getIconServer() > 0);
        }
    }

    @Test
    public void testGetPublicList() throws FlickrException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection<Contact> contacts = iface.getPublicList(testProperties.getNsid());
        assertNotNull(contacts);
        assertTrue("No Contacts. (You need to have contacts for this test to succceed)", contacts.size() > 0);
        Iterator<Contact> it = contacts.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            Contact contact = (Contact) it.next();
            assertNotNull(contact.getUsername());
            assertNotNull(contact.getId());
            assertTrue(contact.getIconFarm() > 0);
            assertTrue(contact.getIconServer() > 0);
        }
    }

}
