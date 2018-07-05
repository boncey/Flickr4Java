package com.flickr4java.flickr.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Domain information as returned by the stats interface.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class Domain {

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(Domain.class);

    public Domain() {
    }

    public Domain(String name, Long views) {
        super();
        this.name = name;
        this.views = views;
    }

    private String name;

    private Long views;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        return String.format("%s (%d)", name, views);
    }
}
