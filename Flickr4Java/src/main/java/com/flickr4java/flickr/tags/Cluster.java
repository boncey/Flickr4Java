package com.flickr4java.flickr.tags;

import java.util.ArrayList;

/**
 * Cluster (list) of tags.
 * 
 * @author mago
 * @since 1.2
 * @version $Id: Cluster.java,v 1.2 2009/07/12 22:43:07 x-mago Exp $
 */
public class Cluster {

    private ArrayList<Tag> tags = new ArrayList<Tag>();

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
