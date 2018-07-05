package com.flickr4java.flickr.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stats information as returned by the stats interface.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class Stats {

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(Stats.class);

    private int views;

    private int comments;

    private int favorites;

    public Stats() {
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setViews(String views) {
        try {
            setViews(Integer.parseInt(views));
        } catch (NumberFormatException e) {
            // ignore and set value as 0
            setViews(0);
        }
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setComments(String comments) {
        try {
            setComments(Integer.parseInt(comments));
        } catch (NumberFormatException e) {
            // ignore and set value as 0
            setComments(0);
        }
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public void setFavorites(String favorites) {
        try {
            setFavorites(Integer.parseInt(favorites));
        } catch (NumberFormatException e) {
            // ignore and set value as null
            setFavorites(0);
        }
    }

    @SuppressWarnings("boxing")
    @Override
    public String toString() {

        return String.format("views (%d), favorites (%d), comments (%d)", views, favorites, comments);
    }
}
