/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.commons.CommonsInterface;
import com.flickr4java.flickr.commons.Institution;

/**
 * @author mago
 * @version $Id: CommonsInterfaceTest.java,v 1.1 2009/06/30 18:48:59 x-mago Exp $
 */
public class CommonsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetInstitutions() throws FlickrException {
        CommonsInterface iface = flickr.getCommonsInterface();
        List<Institution> list = iface.getInstitutions();
        assertNotNull(list);
        boolean museumFound = false;
        for (Institution inst : list) {
            if (inst.getName().equals("Brooklyn Museum")) {
                assertEquals(1211958000000L, inst.getDateLaunch().getTime());
                assertEquals("http://www.brooklynmuseum.org/", inst.getSiteUrl());
                assertEquals("http://www.brooklynmuseum.org/flickr_commons.php", inst.getLicenseUrl());
                assertEquals("http://flickr.com/photos/brooklyn_museum/", inst.getFlickrUrl());
                museumFound = true;
            }
        }
        assertTrue(museumFound);
    }
}
