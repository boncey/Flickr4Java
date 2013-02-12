/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.licenses.License;
import com.flickr4java.flickr.photos.licenses.LicensesInterface;

/**
 * @author Anthony Eden
 */
public class LicensesInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetInfo() throws FlickrException {
        LicensesInterface iface = flickr.getLicensesInterface();
        Collection<License> licenses = iface.getInfo();
        assertNotNull(licenses);
        assertTrue(licenses.size() > 0);
        for (License license : licenses) {
            assertNotNull(license.getId());
            assertNotNull(license.getName());
            assertNotNull(license.getUrl());
        }
    }
}
