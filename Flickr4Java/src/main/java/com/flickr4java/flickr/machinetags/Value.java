package com.flickr4java.flickr.machinetags;

import java.util.Date;

/**
 * 
 * @author mago
 * @version $Id: Value.java,v 1.3 2009/07/12 22:43:07 x-mago Exp $
 */
public class Value {

    String value;

    int usage;

    String namespace;

    String predicate;

    Date firstAdded;

    Date lastAdded;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getUsage() {
        return usage;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public void setFirstAdded(Date date) {
        firstAdded = date;
    }

    public void setFirstAdded(long datePosted) {
        setFirstAdded(new Date(datePosted));
    }

    public void setFirstAdded(String timestamp) {
        if (timestamp == null || "".equals(timestamp))
            return;
        setFirstAdded(Long.parseLong(timestamp) * 1000);
    }

    public void setLastAdded(Date date) {
        lastAdded = date;
    }

    public void setLastAdded(long date) {
        setLastAdded(new Date(date));
    }

    public void setLastAdded(String timestamp) {
        if (timestamp == null || "".equals(timestamp))
            return;
        setLastAdded(Long.parseLong(timestamp) * 1000);
    }

    public void setUsage(String predicates) {
        try {
            setUsage(Integer.parseInt(predicates));
        } catch (NumberFormatException e) {
        }
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {

        return String.format("%s:%s=%s", namespace, predicate, value);
    }
}
