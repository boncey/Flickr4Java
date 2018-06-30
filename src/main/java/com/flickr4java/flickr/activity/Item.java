package com.flickr4java.flickr.activity;

import java.util.Collection;

/**
 * Item with activity can be of type 'photo' or 'photoset'. It contains a list of Event.
 * 
 * @see com.flickr4java.flickr.activity.Event
 * @author mago
 * @version $Id: Item.java,v 1.2 2007/07/22 16:18:20 x-mago Exp $
 */
public class Item {
    private String type;

    private String id;

    private String title;

    private String owner;

    private String secret;

    private String server;

    private String farm;

    // userComments
    private int comments = 0;

    private int notes = 0;

    // userPhotos
    private int commentsOld = 0;

    private int commentsNew = 0;

    private int notesOld = 0;

    private int notesNew = 0;

    private int views = 0;

    private int faves = 0;

    private int more = 0;

    private Collection<Event> events;

    public Item() {
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public int getFaves() {
        return faves;
    }

    public void setFaves(int faves) {
        this.faves = faves;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public void setEvents(Collection<Event> events) {
        this.events = events;
    }

    /**
     * Available if delivered by ActivityInterface.userComments()
     * 
     * @return number of comments
     */
    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * Available if delivered by ActivityInterface.userComments()
     * 
     * @return number of notes
     */
    public int getNotes() {
        return notes;
    }

    public void setNotes(int notes) {
        this.notes = notes;
    }

    /**
     * Available if delivered by ActivityInterface.userPhotos()
     * 
     * @return number of new comments
     */
    public int getCommentsNew() {
        return commentsNew;
    }

    public void setCommentsNew(int commentsNew) {
        this.commentsNew = commentsNew;
    }

    /**
     * Available if delivered by ActivityInterface.userPhotos()
     * 
     * @return number of old comments
     */
    public int getCommentsOld() {
        return commentsOld;
    }

    public void setCommentsOld(int commentsOld) {
        this.commentsOld = commentsOld;
    }

    /**
     * Available if delivered by ActivityInterface.userPhotos()
     * 
     * @return number of new notes
     */
    public int getNotesNew() {
        return notesNew;
    }

    public void setNotesNew(int notesNew) {
        this.notesNew = notesNew;
    }

    /**
     * Available if delivered by ActivityInterface.userPhotos()
     * 
     * @return number of old notes
     */
    public int getNotesOld() {
        return notesOld;
    }

    public void setNotesOld(int notesOld) {
        this.notesOld = notesOld;
    }

}
