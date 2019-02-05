

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.commons.CommonsInterface;
import com.flickr4java.flickr.commons.Institution;

import org.junit.Test;

import java.util.List;

/**
 * @author mago
 * @version $Id: CommonsInterfaceTest.java,v 1.1 2009/06/30 18:48:59 x-mago Exp $
 */
public class CommonsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetInstitutions() throws FlickrException {
        CommonsInterface iface = flickr.getCommonsInterface();
        List<Institution> list = iface.getInstitutions();
        assertNotNull(list);
        boolean museumFound = false;
        for (Institution inst : list) {
            if (inst.getName().equals("The British Library")) {
                assertEquals(1386975388000L, inst.getDateLaunch().getTime());
                assertEquals("www.bl.uk", inst.getSiteUrl());
                assertEquals("http://www.bl.uk/aboutus/terms/copyright/index.html", inst.getLicenseUrl());
                assertEquals("http://flickr.com/photos/britishlibrary/", inst.getFlickrUrl());
                museumFound = true;
            }
        }
        assertTrue(museumFound);
    }
}
