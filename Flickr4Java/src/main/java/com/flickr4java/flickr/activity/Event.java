package com.flickr4java.flickr.activity;

import java.util.Date;

/**
 * Activity-Event. It's type is either 'note' or 'comment', or 'fave'. id is set only, if the type of the Event is 'note' or 'comment'.
 * 
 * @see com.flickr4java.flickr.activity.Item
 * @author mago
 * @version $Id: Event.java,v 1.2 2007/07/22 16:18:20 x-mago Exp $
 */
public class Event {
    private String id;

    private String type;

    private String user;

    private String username;

    private String value;

    private Date dateadded;

    public Event() {
    }

    public Date getDateadded() {
        return dateadded;
    }

    public void setDateadded(Date dateadded) {
        this.dateadded = dateadded;
    }

    public void setDateadded(String dateAdded) {
        if (dateAdded == null || "".equals(dateAdded))
            return;
        setDateadded(new Date(Long.parseLong(dateAdded) * (long) 1000));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
