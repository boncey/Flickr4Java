package com.flickr4java.flickr.stats;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 * For the flickr.stats.getCSVFiles call.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class Csv {

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(Csv.class);

    private String href;

    private String type;

    private Date date;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
