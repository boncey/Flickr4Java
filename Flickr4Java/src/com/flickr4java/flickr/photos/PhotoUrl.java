/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.photos;

/**
 * @author Anthony Eden
 */
public class PhotoUrl {

    private String url;

    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Possible known type is photopage.
     * 
     * @return Type of URL
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
