package com.flickr4java.flickr.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.activity.ActivityInterface;
import com.flickr4java.flickr.activity.Item;
import com.flickr4java.flickr.activity.ItemList;

/**
 * 
 * @author mago
 * @version $Id: ActivityInterfaceTest.java,v 1.3 2009/06/30 18:48:59 x-mago Exp $
 */
public class ActivityInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testUserComments() throws FlickrException {
        ActivityInterface actInterface = flickr.getActivityInterface();
        ItemList<Item> list = actInterface.userComments(10, 1);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testUserPhotos() throws FlickrException {
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
