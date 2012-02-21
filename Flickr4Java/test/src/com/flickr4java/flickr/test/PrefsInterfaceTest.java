package com.flickr4java.flickr.test;

import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.prefs.PrefsInterface;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

/**
 * @author Martin Goebel
 * @version $Id: PrefsInterfaceTest.java,v 1.3 2008/06/28 22:30:04 x-mago Exp $
 */
public class PrefsInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
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
    public void testGetContentType() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        String type = iface.getContentType();
        assertTrue(type.equals(Flickr.CONTENTTYPE_OTHER) || type.equals(Flickr.CONTENTTYPE_PHOTO) || type.equals(Flickr.CONTENTTYPE_SCREENSHOT));
    }

    @Test
    public void testGetSafetyLevel() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        String level = iface.getSafetyLevel();
        assertTrue(level.equals(Flickr.SAFETYLEVEL_SAFE) || level.equals(Flickr.SAFETYLEVEL_MODERATE) || level.equals(Flickr.SAFETYLEVEL_RESTRICTED));
    }

    @Test
    public void testGetHidden() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        Boolean hidden = iface.getHidden();
    }

    @Test
    public void testGetGeoPerms() throws FlickrException, IOException, SAXException {
        PrefsInterface iface = flickr.getPrefsInterface();
        int geoPerm = iface.getGeoPerms();
        // check for known levels.
        if (geoPerm != Flickr.PRIVACY_LEVEL_NO_FILTER && geoPerm != Flickr.PRIVACY_LEVEL_FRIENDS && geoPerm != Flickr.PRIVACY_LEVEL_PUBLIC
                && geoPerm != Flickr.PRIVACY_LEVEL_PRIVATE && geoPerm != Flickr.PRIVACY_LEVEL_FRIENDS_FAMILY && geoPerm != Flickr.PRIVACY_LEVEL_FAMILY) {
            assertTrue(false);
        }
    }
}
