package com.flickr4java.flickr.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.photos.Photo;

import org.junit.Test;

/**
 * 
 * @author mago
 * @version $Id: ObjectTest.java,v 1.1 2009/07/23 20:41:03 x-mago Exp $
 */
public class ObjectTest {
    /**
     * Testing the equals-implementation.
     * 
     * Don't test every single member, as they are covered in equals() by reflection. Emphasis on the child-objects.
     */
    @Test
    public void testPhoto() {
        Photo p1 = new Photo();
        Photo p2 = new Photo();
        assertTrue(p1.equals(p2));

        p1.setSecret("secret");
        assertFalse(p1.equals(p2));
        p2.setSecret("secret");
        assertTrue(p1.equals(p2));
    }
}
