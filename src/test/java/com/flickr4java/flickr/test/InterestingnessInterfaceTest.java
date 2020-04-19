package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.interestingness.InterestingnessInterface;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

import org.junit.Test;

/**
 * 
 * @version $Id: InterestingnessInterfaceTest.java,v 1.5 2008/01/28 23:01:45 x-mago Exp $
 */
public class InterestingnessInterfaceTest extends Flickr4JavaTest {

    /*
     * Test method for 'com.flickr4java.flickr.test.interestingness.InterestingnessInterface.getList(String, Set, int, int)'
     */
    @Test
    public void testGetListStringSetIntInt() throws FlickrException {
        assertNotNull(flickr);
        InterestingnessInterface ii = flickr.getInterestingnessInterface();
        assertNotNull(ii);
        PhotoList<Photo> list = ii.getList("2006-09-11", Extras.ALL_EXTRAS, 7, 9);
        assertNotNull(list);
        assertEquals(7, list.size());
        assertEquals(9, list.getPage());
        assertEquals(7, list.getPerPage());
        assertEquals(500, list.getTotal());
        assertTrue(list.get(0) instanceof Photo);
        Photo photo = list.get(1);
        assertNotNull(photo.getId());
        assertNotNull(photo.getLicense());
        assertNotNull(photo.getOwner());

        list = ii.getList("2006-09-11", null, 500, 1);
        assertNotNull(list);
        assertTrue(list.size() > 0);

        list = ii.getList((String) null, Extras.ALL_EXTRAS, 100, 1);
        assertNotNull(list);
        assertTrue(list.size() > 1);
        photo = list.get(0);
        for (int i = list.size() - 1; i >= 0; --i) {
            photo = list.get(i);
            if (photo.hasGeoData()) {
                // System.out.println(photo.getId() + " " + photo.getGeoData() + " " + photo.getUrl());
            }
        }
    }

    /*
     * Test method for 'com.flickr4java.flickr.test.interestingness.InterestingnessInterface.getList(Date, Set, int, int)'
     */
    @Test
    public void testGetListDateSetIntInt() {

    }

    /*
     * Test method for 'com.flickr4java.flickr.test.interestingness.InterestingnessInterface.getList()'
     */
    @Test
    public void testGetList() {

    }

}
