/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.flickr4java.flickr.panda.Panda;
import com.flickr4java.flickr.panda.PandaInterface;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author mago
 * @version $Id: PandaInterfaceTest.java,v 1.1 2009/06/18 21:56:43 x-mago Exp $
 */
public class PandaInterfaceTest extends TestCase {

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

    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        PandaInterface iface = flickr.getPandaInterface();
        Panda p = new Panda();
        p.setName("ling ling");
        PhotoList list = iface.getPhotos(p, null, 1, 50);
        assertNotNull(list);
    }
}
