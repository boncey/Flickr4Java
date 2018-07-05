package com.flickr4java.flickr.collections;

import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photosets.Photoset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * As returned by calls to flickr.collections.* methods.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class Collection {

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(Collection.class);

    private String id;

    private String title;

    private String description;

    private int childCount;

    private Date dateCreated;

    private String iconLarge;

    private String iconSmall;

    private String server;

    private String secret;

    private List<Collection> collections;

    private List<Photoset> photosets;

    private List<Photo> photos;

    public Collection() {

        photos = new ArrayList<Photo>();
        photosets = new ArrayList<Photoset>();
        collections = new ArrayList<Collection>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public void setChildCount(String childCount) {
        if (childCount != null && childCount.length() > 0) {
            setChildCount(Integer.parseInt(childCount));
        }
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateCreated(long dateCreatedLong) {
        setDateCreated(new Date(dateCreatedLong));
    }

    public void setDateCreated(String dateCreatedString) {
        if (dateCreatedString != null && dateCreatedString.length() > 0) {
            setDateCreated(Long.parseLong(dateCreatedString) * 1000);
        }
    }

    public String getIconLarge() {
        return iconLarge;
    }

    public void setIconLarge(String iconLarge) {
        this.iconLarge = iconLarge;
    }

    public String getIconSmall() {
        return iconSmall;
    }

    public void setIconSmall(String iconSmall) {
        this.iconSmall = iconSmall;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Photoset> getPhotosets() {
        return photosets;
    }

    public void setPhotosets(List<Photoset> photosets) {
        this.photosets = photosets;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public void addPhotoset(Photoset photoset) {
        photosets.add(photoset);
    }

    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", title, id);
    }
}
