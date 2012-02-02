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

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

/**
 *
 * @author mago
 * @version $Id: ActivityInterfaceTest.java,v 1.3 2009/06/30 18:48:59 x-mago Exp $
 */
public class ActivityInterfaceTest {

    Flickr flickr = null;
    private TestProperties testProperties;


    @Before
    public void setUp() throws
    ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;
        testProperties = new TestProperties();

        REST rest = new REST();

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
