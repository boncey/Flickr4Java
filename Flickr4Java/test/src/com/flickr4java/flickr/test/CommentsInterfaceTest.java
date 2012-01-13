package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.comments.Comment;
import com.flickr4java.flickr.photos.comments.CommentsInterface;
import com.flickr4java.flickr.util.IOUtilities;
/**
 *
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: CommentsInterfaceTest.java,v 1.7 2009/06/30 18:48:59 x-mago Exp $
 */
public class CommentsInterfaceTest extends TestCase {
    Flickr flickr = null;
    Properties properties = null;

    public CommentsInterfaceTest(String arg0) {
        super(arg0);
    }

	protected void setUp() throws Exception {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

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

    public void testGetList() throws IOException, SAXException, FlickrException {
        String photoId = "245253195"; // http://www.flickr.com/photos/extranoise/245253195/
        CommentsInterface ci = flickr.getCommentsInterface();
        List comments = ci.getList(photoId);
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
        Iterator commentsIterator = comments.iterator();

        while (commentsIterator.hasNext()) {
            Comment comment = (Comment)commentsIterator.next();
            assertNotNull(comment.getId());
            assertNotNull(comment.getAuthor());
            assertNotNull(comment.getAuthorName());
            assertNotNull(comment.getDateCreate());
            assertNotNull(comment.getPermaLink());
            assertNotNull(comment.getText());
        }
    }

    public void testComment() throws IOException, SAXException, FlickrException {
        String photoId = "419231219"; // http://www.flickr.com/photos/javatest3/419231219/
        String txt1 = "This is a test for the flickr java api";
        String txt2 = "This is an edited comment for the java flickr api";
        CommentsInterface ci = flickr.getCommentsInterface();
        // add a comment
        String commentId = ci.addComment(photoId, txt1);
        //System.out.println("Comment Id:" + commentId);
        assertNotNull(commentId);
        assertTrue(commentId.length() > 0);
        // verify if comment arrived on the photo page
        Comment comment = findCommment(photoId, commentId);
        assertNotNull(comment);
        assertEquals(commentId, comment.getId());
        assertEquals(txt1, comment.getText());
        // change the comment text and verify change
        ci.editComment(commentId, txt2);
        comment = findCommment(photoId, commentId);
        assertNotNull(comment);
        assertEquals(commentId, comment.getId());
        assertEquals(txt2, comment.getText());
        // delete the comment
        ci.deleteComment(commentId);
        comment = findCommment(photoId, commentId);
        assertNull(comment);
    }

    // helper function to find a comment by it's id for a specified photo
    private Comment findCommment(String photoId, String commentId) throws FlickrException, IOException, SAXException {
        CommentsInterface ci = flickr.getCommentsInterface();
        List comments = ci.getList(photoId);
        Iterator commentsIterator = comments.iterator();

        while (commentsIterator.hasNext()) {
            Comment comment = (Comment)commentsIterator.next();
            if (comment.getId().equals(commentId)) {
                return comment;
            }
        }
        return null;
    }

    public void testGetRecentForContacts() throws IOException, SAXException, FlickrException {
        CommentsInterface ci = flickr.getCommentsInterface();
        PhotoList photos = ci.getRecentForContacts(null, null, Extras.ALL_EXTRAS, 50, 1);
        assertTrue(photos != null);
    }
}
