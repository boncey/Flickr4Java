/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

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
import com.flickr4java.flickr.photos.licenses.License;
import com.flickr4java.flickr.photos.licenses.LicensesInterface;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class LicensesInterfaceTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugStream = true;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
            properties.load(in);

OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
    				.apiSecret(properties.getProperty("secret")).build();
            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), rest);

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
