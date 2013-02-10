/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.flickr4java.flickr.photos.licenses.License;
import com.flickr4java.flickr.photos.licenses.LicensesInterface;

/**
 * @author Anthony Eden
 */
public class LicensesInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, FlickrException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        SOAP soap = new SOAP(testProperties.getHost());
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
