/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.panda.Panda;
import com.flickr4java.flickr.panda.PandaInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

import org.junit.Test;

import java.util.ArrayList;

/**
 * @author mago
 * @version $Id: PandaInterfaceTest.java,v 1.1 2009/06/18 21:56:43 x-mago Exp $
 */
public class PandaInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetList() throws FlickrException {
        PandaInterface iface = flickr.getPandaInterface();
        ArrayList<Panda> list = iface.getList();
        assertNotNull(list);
        Panda p = list.get(0);
        assertEquals("ling ling", p.getName());
        p = list.get(1);
        assertEquals("hsing hsing", p.getName());
        p = list.get(2);
        assertEquals("wang wang", p.getName());
    }

    @Test
    public void testGetPhotos() throws FlickrException {
        PandaInterface iface = flickr.getPandaInterface();
        Panda p = new Panda();
        p.setName("ling ling");
        PhotoList<Photo> list = iface.getPhotos(p, null, 1, 50);
        assertNotNull(list);
    }
}
