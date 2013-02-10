/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

import java.util.Date;

/**
 * @author Anthony Eden
 */
public class Photocount {

    private int count;

    private Date fromDate;

    private Date toDate;

    public Photocount() {

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCount(String count) {
        if (count != null)
            setCount(Integer.parseInt(count));
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setFromDate(long fromDate) {
        setFromDate(new Date(fromDate));
    }

    public void setFromDate(String fromDate) {
        if (fromDate != null)
            setFromDate(Long.parseLong(fromDate));
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void setToDate(long toDate) {
        setToDate(new Date(toDate));
    }

    public void setToDate(String toDate) {
        if (toDate != null)
            setToDate(Long.parseLong(toDate));
    }

}
