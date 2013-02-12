/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Element;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.people.User;

/**
 * @author Anthony Eden
 * @version $Id: TestInterfaceTest.java,v 1.6 2008/01/28 23:01:45 x-mago Exp $
 */
public class TestInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testEcho() throws FlickrException {
        TestInterface iface = flickr.getTestInterface();
        Map<String, String> params = new HashMap<String, String>();
        params.put("test", "test");
        Collection<Element> results = iface.echo(params);
        assertNotNull(results);
    }

    @Test
    public void testLogin() throws FlickrException {
        TestInterface iface = flickr.getTestInterface();
        User user = iface.login();
        assertNotNull(user);
    }

}
