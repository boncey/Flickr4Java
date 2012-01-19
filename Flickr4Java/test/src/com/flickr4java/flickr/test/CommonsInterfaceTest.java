/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.flickr4java.flickr.commons.CommonsInterface;
import com.flickr4java.flickr.commons.Institution;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author mago
 * @version $Id: CommonsInterfaceTest.java,v 1.1 2009/06/30 18:48:59 x-mago Exp $
 */
public class CommonsInterfaceTest extends TestCase {
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

    public void testGetInstitutions() throws FlickrException, IOException, SAXException {
        CommonsInterface iface = flickr.getCommonsInterface();
        ArrayList list = iface.getInstitutions();
        assertNotNull(list);
        Iterator it = list.iterator();
        boolean museumFound = false;
        while (it.hasNext()) {
            Institution inst = (Institution) it.next();
            if (inst.getName().equals("Brooklyn Museum")) {
                assertEquals(
                    1211958000000L,
                    inst.getDateLaunch().getTime()
                );
                assertEquals(
                    "http://www.brooklynmuseum.org/",
                    inst.getSiteUrl()
                );
                assertEquals(
                    "http://www.brooklynmuseum.org/flickr_commons.php",
                    inst.getLicenseUrl()
                );
                assertEquals(
                    "http://flickr.com/photos/brooklyn_museum/",
                    inst.getFlickrUrl()
                );
                museumFound = true;
            }
        }
        assertTrue(museumFound);
    }
}
