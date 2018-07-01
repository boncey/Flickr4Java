package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.comments.Comment;
import com.flickr4java.flickr.photos.comments.CommentsInterface;

/**
 * 
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: CommentsInterfaceTest.java,v 1.7 2009/06/30 18:48:59 x-mago Exp $
 */
public class CommentsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetList() throws FlickrException {
        String photoId = "245253195"; // http://www.flickr.com/photos/extranoise/245253195/
        CommentsInterface ci = flickr.getCommentsInterface();
        List<Comment> comments = ci.getList(photoId);
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
        Iterator<Comment> commentsIterator = comments.iterator();

        while (commentsIterator.hasNext()) {
            Comment comment = (Comment) commentsIterator.next();
            assertNotNull(comment.getId());
            assertNotNull(comment.getAuthor());
            assertNotNull(comment.getAuthorName());
            assertNotNull(comment.getDateCreate());
            assertNotNull(comment.getPermaLink());
            assertNotNull(comment.getText());
        }
    }

    @Test
    public void testComment() throws FlickrException {
        String photoId = testProperties.getPhotoId(); // http://www.flickr.com/photos/javatest3/419231219/
        String txt1 = "This is a test for the flickr java api";
        String txt2 = "This is an edited comment for the java flickr api";
        CommentsInterface ci = flickr.getCommentsInterface();
        // add a comment
        String commentId = ci.addComment(photoId, txt1);
        // System.out.println("Comment Id:" + commentId);
        assertNotNull(commentId);
        assertTrue(commentId.length() > 0);

        // change the comment text and verify change
        ci.editComment(commentId, txt2);

        // delete the comment
        ci.deleteComment(commentId);
        Comment comment = findCommment(photoId, commentId);
        assertNull(comment);
    }

    // helper function to find a comment by it's id for a specified photo
    private Comment findCommment(String photoId, String commentId) throws FlickrException {
        CommentsInterface ci = flickr.getCommentsInterface();
        List<Comment> comments = ci.getList(photoId);
        Iterator<Comment> commentsIterator = comments.iterator();

        while (commentsIterator.hasNext()) {
            Comment comment = (Comment) commentsIterator.next();
            if (comment.getId().equals(commentId)) {
                return comment;
            }
        }
        return null;
    }

    @Test
    public void testGetRecentForContacts() throws FlickrException {
        CommentsInterface ci = flickr.getCommentsInterface();
        PhotoList<Photo> photos = ci.getRecentForContacts(null, null, Extras.ALL_EXTRAS, 50, 1);
        assertTrue(photos != null);
    }
}
