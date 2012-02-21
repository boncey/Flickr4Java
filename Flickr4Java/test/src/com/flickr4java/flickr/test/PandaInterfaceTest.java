/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.panda.Panda;
import com.flickr4java.flickr.panda.PandaInterface;
import com.flickr4java.flickr.photos.PhotoList;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author mago
 * @version $Id: PandaInterfaceTest.java,v 1.1 2009/06/18 21:56:43 x-mago Exp $
 */
public class PandaInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        // Flickr.debugStream = true;

        testProperties = new TestProperties();

        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(testProperties.getApiKey()).apiSecret(testProperties.getSecret()).build();
        REST rest = new REST();
        rest.setHost(testProperties.getHost());

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testGetList() throws FlickrException, IOException, SAXException {
        PandaInterface iface = flickr.getPandaInterface();
        ArrayList list = iface.getList();
        assertNotNull(list);
        Panda p = (Panda) list.get(0);
        assertEquals("ling ling", p.getName());
        p = (Panda) list.get(1);
        assertEquals("hsing hsing", p.getName());
        p = (Panda) list.get(2);
        assertEquals("wang wang", p.getName());
    }

    @Test
    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        PandaInterface iface = flickr.getPandaInterface();
        Panda p = new Panda();
        p.setName("ling ling");
        PhotoList list = iface.getPhotos(p, null, 1, 50);
        assertNotNull(list);
    }
}
