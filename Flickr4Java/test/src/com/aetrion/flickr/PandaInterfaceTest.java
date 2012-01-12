/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
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

            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(
                properties.getProperty("apiKey"),
                properties.getProperty("secret"),
                rest
            );

            RequestContext requestContext = RequestContext.getRequestContext();

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            requestContext.setAuth(auth);
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
