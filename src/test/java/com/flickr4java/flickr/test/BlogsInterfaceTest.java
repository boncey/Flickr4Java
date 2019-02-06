

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.blogs.Blog;
import com.flickr4java.flickr.blogs.BlogsInterface;
import com.flickr4java.flickr.blogs.Service;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Anthony Eden
 */
public class BlogsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetList() throws FlickrException {
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection<Blog> blogs = blogsInterface.getList();
        assertNotNull(blogs);
    }

    @Test
    public void testGetServices() throws FlickrException {
        BlogsInterface blogsInterface = flickr.getBlogsInterface();
        Collection<Service> services = blogsInterface.getServices();
        Iterator<Service> it = services.iterator();
        boolean bloggerFound = false;
        while (it.hasNext()) {
            Service ser = it.next();
            if (ser.getId().equals("beta.blogger.com") && ser.getName().equals("Blogger")) {
                bloggerFound = true;
            }
            // System.out.println(ser.getId() + " " + ser.getName());
        }
        assertTrue(bloggerFound);
    }

    @Ignore
    @Test
    public void testPostImage() {
        // TODO: implement this test
    }

}
