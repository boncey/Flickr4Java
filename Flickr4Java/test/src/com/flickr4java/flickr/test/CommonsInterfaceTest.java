/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.commons.CommonsInterface;
import com.flickr4java.flickr.commons.Institution;

/**
 * @author mago
 * @version $Id: CommonsInterfaceTest.java,v 1.1 2009/06/30 18:48:59 x-mago Exp $
 */
public class CommonsInterfaceTest {
    Flickr flickr = null;
    private TestProperties testProperties;


    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        //Flickr.debugStream = true;

        testProperties = new TestProperties();

        REST rest = new REST();
        rest.setHost(testProperties.getHost());

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
    public void testGetInstitutions() throws FlickrException, IOException, SAXException {
        CommonsInterface iface = flickr.getCommonsInterface();
        List<Institution> list = iface.getInstitutions();
        assertNotNull(list);
        boolean museumFound = false;
        for (Institution inst : list) {
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
