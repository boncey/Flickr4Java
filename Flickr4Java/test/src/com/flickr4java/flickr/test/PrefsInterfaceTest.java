package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.prefs.PrefsInterface;

/**
 * @author Martin Goebel
 * @version $Id: PrefsInterfaceTest.java,v 1.3 2008/06/28 22:30:04 x-mago Exp $
 */
public class PrefsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetContentType() throws FlickrException {
        PrefsInterface iface = flickr.getPrefsInterface();
        String type = iface.getContentType();
        assertTrue(type.equals(Flickr.CONTENTTYPE_OTHER) || type.equals(Flickr.CONTENTTYPE_PHOTO) || type.equals(Flickr.CONTENTTYPE_SCREENSHOT));
    }

    @Test
    public void testGetSafetyLevel() throws FlickrException {
        PrefsInterface iface = flickr.getPrefsInterface();
        String level = iface.getSafetyLevel();
        assertTrue(level.equals(Flickr.SAFETYLEVEL_SAFE) || level.equals(Flickr.SAFETYLEVEL_MODERATE) || level.equals(Flickr.SAFETYLEVEL_RESTRICTED));
    }

    @Test
    public void testGetHidden() throws FlickrException {
        PrefsInterface iface = flickr.getPrefsInterface();
        Boolean hidden = iface.getHidden();
        assertNotNull(hidden);
    }

    @Test
    public void testGetGeoPerms() throws FlickrException {
        PrefsInterface iface = flickr.getPrefsInterface();
        int geoPerm = iface.getGeoPerms();
        // check for known levels.
        if (geoPerm != Flickr.PRIVACY_LEVEL_NO_FILTER && geoPerm != Flickr.PRIVACY_LEVEL_FRIENDS && geoPerm != Flickr.PRIVACY_LEVEL_PUBLIC
                && geoPerm != Flickr.PRIVACY_LEVEL_PRIVATE && geoPerm != Flickr.PRIVACY_LEVEL_FRIENDS_FAMILY && geoPerm != Flickr.PRIVACY_LEVEL_FAMILY) {
            assertTrue(false);
        }
    }
}
