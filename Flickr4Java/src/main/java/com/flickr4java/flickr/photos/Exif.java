/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.photos;

/**
 * @author Anthony Eden
 */
public class Exif {

    private String tagspace;

    private String tagspaceId;

    private String tag;

    private String label;

    private String raw;

    private String clean;

    public String getTagspace() {
        return tagspace;
    }

    public void setTagspace(String tagspace) {
        this.tagspace = tagspace;
    }

    public String getTagspaceId() {
        return tagspaceId;
    }

    public void setTagspaceId(String tagspaceId) {
        this.tagspaceId = tagspaceId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getClean() {
        return clean;
    }

    public void setClean(String clean) {
        this.clean = clean;
    }

}
