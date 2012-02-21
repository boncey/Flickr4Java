package com.flickr4java.flickr.stats;

import org.apache.log4j.Logger;

/**
 * Referrer information as returned by the stats interface.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class Referrer {

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(Referrer.class);

    public Referrer() {
    }

    public Referrer(String url, Long views) {
        super();
        this.url = url;
        this.views = views;
    }

    private String url;

    private Long views;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public void setViews(String views) {

        try {
            setViews(new Long(views));
        } catch (NumberFormatException e) {
            // ignore and set value as null
        }
    }

    @Override
    public String toString() {

        return String.format("%s (%d)", url, views);
    }
}
