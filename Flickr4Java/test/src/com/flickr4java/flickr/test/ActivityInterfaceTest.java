package com.flickr4java.flickr.test;

import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.activity.ActivityInterface;
import com.flickr4java.flickr.activity.ItemList;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author mago
 * @version $Id: ActivityInterfaceTest.java,v 1.3 2009/06/30 18:48:59 x-mago Exp $
 */
public class ActivityInterfaceTest {

    Flickr flickr = null;
    Properties properties = null;

    
    @Before
    public void setUp() throws
    ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();

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

    @Test
    public void testUserComments()
            throws FlickrException, IOException, SAXException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList list = actInterface.userComments(10, 1);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testUserPhotos()
            throws FlickrException, IOException, SAXException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList list = actInterface.userPhotos(10, 1, "6000d");
        assertTrue(list.size() > 0);
    }

    @Test
    public void testCheckTimeframeArg() {
        ActivityInterface actInterface = flickr.getActivityInterface();
        assertTrue(actInterface.checkTimeframeArg("300d"));
    }
}
