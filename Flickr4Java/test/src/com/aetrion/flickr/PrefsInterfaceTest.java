package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.prefs.PrefsInterface;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Martin Goebel
 * @version $Id: PrefsInterfaceTest.java,v 1.3 2008/06/28 22:30:04 x-mago Exp $
 */
public class PrefsInterfaceTest extends TestCase {

    Flickr flickr = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            Properties properties = new Properties();
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
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testGetContentType() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        String type = iface.getContentType();
        assertTrue(
            type.equals(Flickr.CONTENTTYPE_OTHER)
            || type.equals(Flickr.CONTENTTYPE_PHOTO)
            || type.equals(Flickr.CONTENTTYPE_SCREENSHOT)
        );
    }

    public void testGetSafetyLevel() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        String level = iface.getSafetyLevel();
        assertTrue(
            level.equals(Flickr.SAFETYLEVEL_SAFE)
            || level.equals(Flickr.SAFETYLEVEL_MODERATE)
            || level.equals(Flickr.SAFETYLEVEL_RESTRICTED)
        );
    }

    public void testGetHidden() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        Boolean hidden = iface.getHidden();
    }

    public void testGetGeoPerms() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        int geoPerm = iface.getGeoPerms();
        // check for known levels.
        if (
            geoPerm != Flickr.PRIVACY_LEVEL_NO_FILTER &&
            geoPerm != Flickr.PRIVACY_LEVEL_FRIENDS &&
            geoPerm != Flickr.PRIVACY_LEVEL_PUBLIC &&
            geoPerm != Flickr.PRIVACY_LEVEL_PRIVATE &&
            geoPerm != Flickr.PRIVACY_LEVEL_FRIENDS_FAMILY &&
            geoPerm != Flickr.PRIVACY_LEVEL_FAMILY
        ) {
            assertTrue(false);
        }
    }
}
