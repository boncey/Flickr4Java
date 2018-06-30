package com.flickr4java.flickr.photos.comments;

import java.util.Date;

/**
 * Encapsulated Photo comments.
 * 
 * @author till (Kill Krech) flickr:extranoise
 * @version $Id: Comment.java,v 1.2 2009/07/12 22:43:07 x-mago Exp $
 */
public class Comment {

    private String id;

    private String author;

    private String authorName;

    private Date dateCreate;

    private String permaLink;

    private String text;

    /**
     * @return the nsid of the comment author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the nsis of the comment author. Not to be called from user code. Will be set by querying flickr.
     * 
     * @param author
     *            nsid of the user
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the name of the author of the comment
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets the commment author name. Not to be called from user code. Will be set by querying flickr.
     * 
     * @param authorName
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * 
     * @return the date, when the comment was created
     */
    public Date getDateCreate() {
        return dateCreate;
    }

    /**
     * Sets the date when the comment was created. Not to be called from user code. Will be set by querying flickr.
     * 
     * @param dateCreate
     *            date,when the comment was created
     */
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     * @return the unique comment id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique id of the comment. Not to be called from user code. Will be set by querying flickr.
     * 
     * @param id
     *            unique comment id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return a link to the comment on the photo page.
     */
    public String getPermaLink() {
        return permaLink;
    }

    /**
     * sets the link to the comment on the photo page. Not to be called from user code. Will be set by querying flickr.
     */
    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    /**
     * @return the text of the comment with possible HTML markup. Not to be called from user code. Will be set by querying flickr.
     */
    public String getText() {
        return text;
    }

    /**
     * sets the comment text.
     * 
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }
}
