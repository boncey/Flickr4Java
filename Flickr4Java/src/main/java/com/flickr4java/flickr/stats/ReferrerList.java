package com.flickr4java.flickr.stats;

import com.flickr4java.flickr.SearchResultList;

/**
 * 
 * @author Darren Greaves
 */
public class ReferrerList extends SearchResultList<Referrer> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
