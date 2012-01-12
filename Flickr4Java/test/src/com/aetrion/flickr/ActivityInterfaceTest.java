package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.activity.ActivityInterface;
import com.flickr4java.flickr.activity.ItemList;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;

/**
 *
 * @author mago
 * @version $Id: ActivityInterfaceTest.java,v 1.3 2009/06/30 18:48:59 x-mago Exp $
 */
public class ActivityInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

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

            RequestContext requestContext = RequestContext.getRequestContext();
            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            auth.setPermission(Permission.READ);
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testUserComments()
      throws FlickrException, IOException, SAXException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList list = actInterface.userComments(10, 1);
        assertTrue(list.size() > 0);
    }

    public void testUserPhotos()
      throws FlickrException, IOException, SAXException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList list = actInterface.userPhotos(10, 1, "6000d");
        assertTrue(list.size() > 0);
    }

    public void testCheckTimeframeArg() {
        ActivityInterface actInterface = flickr.getActivityInterface();
        assertTrue(actInterface.checkTimeframeArg("300d"));
    }
}
