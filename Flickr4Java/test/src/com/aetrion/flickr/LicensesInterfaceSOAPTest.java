/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import com.flickr4java.flickr.photos.licenses.LicensesInterface;
import com.flickr4java.flickr.photos.licenses.License;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Auth;
import junit.framework.TestCase;
import org.xml.sax.SAXException;

/**
 * @author Anthony Eden
 */
public class LicensesInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            SOAP soap = new SOAP(properties.getProperty("host"));
            flickr = new Flickr(properties.getProperty("apiKey"), soap);

            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setSharedSecret(properties.getProperty("secret"));

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetInfo() throws FlickrException, IOException, SAXException {
        LicensesInterface iface = flickr.getLicensesInterface();
        Collection licenses = iface.getInfo();
        assertNotNull(licenses);
        assertTrue(licenses.size() > 0);
        Iterator iter = licenses.iterator();
        while (iter.hasNext()) {
            License license = (License) iter.next();
            assertNotNull(license.getId());
            assertNotNull(license.getName());
            assertNotNull(license.getUrl());
        }
    }

}
