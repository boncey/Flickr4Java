package com.flickr4java.flickr.photos.comments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

/**
 * Work on Comments.
 *
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: CommentsInterface.java,v 1.4 2009/07/11 20:30:27 x-mago Exp $
 */
public class CommentsInterface {
    public static final String METHOD_ADD_COMMENT    = "flickr.photos.comments.addComment";
    public static final String METHOD_DELETE_COMMENT = "flickr.photos.comments.deleteComment";
    public static final String METHOD_EDIT_COMMENT   = "flickr.photos.comments.editComment";
    public static final String METHOD_GET_LIST       = "flickr.photos.comments.getList";
    public static final String METHOD_GET_RECENT     = "flickr.photos.comments.getRecentForContacts";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public CommentsInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Add comment to a photo as the currently authenticated user.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The id of the photo to add a comment to.
     * @param commentText Text of the comment.
     * @return a unique comment id.
     * @throws SAXException
     * @throws IOException
     * @throws FlickrException
     */
    public String addComment(String photoId, String commentText) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_ADD_COMMENT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("comment_text", commentText));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        //Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element commentElement = (Element)response.getPayload();
        return commentElement.getAttribute("id");
    }

    /**
     * Delete a comment as the currently authenticated user.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param commentId The id of the comment to delete.
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void deleteComment(String commentId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_DELETE_COMMENT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("comment_id", commentId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        //Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters);
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
     * @param commentId The id of the comment to edit.
     * @param commentText Update the comment to this text.
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void editComment(String commentId, String commentText) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_EDIT_COMMENT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("comment_id", commentId));
        parameters.add(new Parameter("comment_text", commentText));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        //Note: This method requires an HTTP POST request.
        Response response = transportAPI.post(transportAPI.getPath(), parameters);
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
     * @param photoId The id of the photo to fetch comments for.
     * @return a List of {@link Comment} objects.
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     */
    public List getList(String photoId)
      throws FlickrException, IOException, SAXException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List comments = new ArrayList();
        Element commentsElement = response.getPayload();
        NodeList commentNodes = commentsElement.getElementsByTagName("comment");
        int n = commentNodes.getLength();
        for (int i = 0; i < n; i++) {
            Comment comment = new Comment();
            Element commentElement = (Element)commentNodes.item(i);
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
     * <p>Returns the list of photos belonging to your contacts that have been commented on recently.</p>
     *
     * <p>There is an emphasis on the recent part with this method, which is 
     * fancy-talk for "in the last hour".</p>
     *
     * <p>It is not meant to be a general purpose, get all the comments ever, 
     * but rather a quick and easy way to bubble up photos that people are 
     * talking about ("about") now.</p>
     *
     * <p>It has the added bonus / side-effect of bubbling up photos a person 
     * may have missed because they were uploaded before the photo owner was 
     * made a contact or the business of life got in the way.</p>
     *
     * This method requires authentication with 'read' permission.
     *
     * @param lastComment Limits the resultset to photos that have been commented on since this date. The default, and maximum, offset is (1) hour. Optional, can be null.
     * @param contactsFilter A list of contact NSIDs to limit the scope of the query to. Optional, can be null.
     * @param extras A list of extra information to fetch for each returned record. Optional, can be null.
     * @param perPage The number of photos per page.
     * @param page The page offset.
     * @return List of photos
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     */
    public PhotoList getRecentForContacts(Date lastComment, ArrayList contactsFilter, Set extras, int perPage, int page) throws FlickrException, IOException, SAXException {
        PhotoList photos = new PhotoList();
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", PhotosInterface.METHOD_GET_NOT_IN_SET));
        parameters.add(new Parameter("api_key", apiKey));

        if (lastComment != null) {
            parameters.add(new Parameter("last_comment", String.valueOf(lastComment.getTime() / 1000L)));
        }

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }

        if (contactsFilter != null && !contactsFilter.isEmpty()) {
            parameters.add(new Parameter("contacts_filter", StringUtilities.join(contactsFilter, ",")));
        }

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
