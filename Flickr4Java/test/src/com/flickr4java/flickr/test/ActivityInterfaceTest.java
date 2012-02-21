package com.flickr4java.flickr.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.activity.ActivityInterface;
import com.flickr4java.flickr.activity.Item;
import com.flickr4java.flickr.activity.ItemList;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;

/**
 * 
 * @author mago
 * @version $Id: ActivityInterfaceTest.java,v 1.3 2009/06/30 18:48:59 x-mago Exp $
 */
public class ActivityInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;
        testProperties = new TestProperties();

        REST rest = new REST();

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
    public void testUserComments() throws FlickrException, IOException, SAXException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList<Item> list = actInterface.userComments(10, 1);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testUserPhotos() throws FlickrException, IOException, SAXException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList<Item> list = actInterface.userPhotos(10, 1, "6000d");
        assertTrue(list.size() > 0);
    }

    @Test
    public void testCheckTimeframeArg() {
        ActivityInterface actInterface = flickr.getActivityInterface();
        assertTrue(actInterface.checkTimeframeArg("300d"));
    }
}
