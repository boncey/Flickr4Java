package com.flickr4java.flickr.photos.comments;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Work on Comments.
 * 
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: CommentsInterface.java,v 1.4 2009/07/11 20:30:27 x-mago Exp $
 */
public class CommentsInterface {
    public static final String METHOD_ADD_COMMENT = "flickr.photos.comments.addComment";

    public static final String METHOD_DELETE_COMMENT = "flickr.photos.comments.deleteComment";

    public static final String METHOD_EDIT_COMMENT = "flickr.photos.comments.editComment";

    public static final String METHOD_GET_LIST = "flickr.photos.comments.getList";

    public static final String METHOD_GET_RECENT = "flickr.photos.comments.getRecentForContacts";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public CommentsInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Add comment to a photo as the currently authenticated user.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The id of the photo to add a comment to.
     * @param commentText
     *            Text of the comment.
     * @return a unique comment id.
     * @throws FlickrException
     */
    public String addComment(String photoId, String commentText) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD_COMMENT);

        parameters.put("photo_id", photoId);
        parameters.put("comment_text", commentText);

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element commentElement = response.getPayload();
        return commentElement.getAttribute("id");
    }

    /**
     * Delete a comment as the currently authenticated user.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param commentId
     *            The id of the comment to delete.
     * @throws FlickrException
     */
    public void deleteComment(String commentId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_DELETE_COMMENT);

        parameters.put("comment_id", commentId);

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty
        // sucess response if it completes without error.
    }

    /**
     * Edit the text of a comment as the currently authenticated user.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param commentId
     *            The id of the comment to edit.
     * @param commentText
     *            Update the comment to this text.
     * @throws FlickrException
     */
    public void editComment(String commentId, String commentText) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_COMMENT);

        parameters.put("comment_id", commentId);
        parameters.put("comment_text", commentText);

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty
        // sucess response if it completes without error.
    }

    /**
     * Returns the comments for a photo.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The id of the photo to fetch comments for.
     * @return a List of {@link Comment} objects.
     * @throws FlickrException
     */
    public List<Comment> getList(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);
        parameters.put("photo_id", photoId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<Comment> comments = new ArrayList<Comment>();
        Element commentsElement = response.getPayload();
        NodeList commentNodes = commentsElement.getElementsByTagName("comment");
        int n = commentNodes.getLength();
        for (int i = 0; i < n; i++) {
            Comment comment = new Comment();
            Element commentElement = (Element) commentNodes.item(i);
            comment.setId(commentElement.getAttribute("id"));
            comment.setAuthor(commentElement.getAttribute("author"));
            comment.setAuthorName(commentElement.getAttribute("authorname"));
            comment.setPermaLink(commentElement.getAttribute("permalink"));
            long unixTime = 0;
            try {
                unixTime = Long.parseLong(commentElement.getAttribute("datecreate"));
            } catch (NumberFormatException e) {
                // what shall we do?
                e.printStackTrace();
            }
            comment.setDateCreate(new Date(unixTime * 1000L));
            comment.setPermaLink(commentElement.getAttribute("permalink"));
            comment.setText(XMLUtilities.getValue(commentElement));
            comments.add(comment);
        }
        return comments;
    }

    /**
     * <p>
     * Returns the list of photos belonging to your contacts that have been commented on recently.
     * </p>
     * 
     * <p>
     * There is an emphasis on the recent part with this method, which is fancy-talk for "in the last hour".
     * </p>
     * 
     * <p>
     * It is not meant to be a general purpose, get all the comments ever, but rather a quick and easy way to bubble up photos that people are talking about
     * ("about") now.
     * </p>
     * 
     * <p>
     * It has the added bonus / side-effect of bubbling up photos a person may have missed because they were uploaded before the photo owner was made a contact
     * or the business of life got in the way.
     * </p>
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param lastComment
     *            Limits the resultset to photos that have been commented on since this date. The default, and maximum, offset is (1) hour. Optional, can be
     *            null.
     * @param contactsFilter
     *            A list of contact NSIDs to limit the scope of the query to. Optional, can be null.
     * @param extras
     *            A list of extra information to fetch for each returned record. Optional, can be null.
     * @param perPage
     *            The number of photos per page.
     * @param page
     *            The page offset.
     * @return List of photos
     * @throws FlickrException
     */
    public PhotoList<Photo> getRecentForContacts(Date lastComment, ArrayList<String> contactsFilter, Set<String> extras, int perPage, int page)
            throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", PhotosInterface.METHOD_GET_NOT_IN_SET);

        if (lastComment != null) {
            parameters.put("last_comment", String.valueOf(lastComment.getTime() / 1000L));
        }

        if (extras != null && !extras.isEmpty()) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }

        if (contactsFilter != null && !contactsFilter.isEmpty()) {
            parameters.put("contacts_filter", StringUtilities.join(contactsFilter, ","));
        }

        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoElements = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoElements.getLength(); i++) {
            Element photoElement = (Element) photoElements.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }
}
