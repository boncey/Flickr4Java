/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.uploader;

import java.util.Collection;

/**
 * Metadata that describe a photo.
 * 
 * @author Anthony Eden
 * @version $Id: UploadMetaData.java,v 1.7 2007/11/02 21:46:52 x-mago Exp $
 */
public class UploadMetaData {

    private String title;

    private String description;

    private Collection<String> tags;

    private boolean publicFlag;

    private boolean friendFlag;

    private boolean familyFlag;

    private boolean async = false;

    private Boolean hidden;

    private String safetyLevel;

    private String contentType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public boolean isPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(boolean publicFlag) {
        this.publicFlag = publicFlag;
    }

    public boolean isFriendFlag() {
        return friendFlag;
    }

    public void setFriendFlag(boolean friendFlag) {
        this.friendFlag = friendFlag;
    }

    public boolean isFamilyFlag() {
        return familyFlag;
    }

    public void setFamilyFlag(boolean familyFlag) {
        this.familyFlag = familyFlag;
    }

    /**
     * Get the Content-type of the Photo.
     * 
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_OTHER
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_PHOTO
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_SCREENSHOT
     * @return contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the Content-type of the Photo.
     * 
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_OTHER
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_PHOTO
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_SCREENSHOT
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Get the safety-level.
     * 
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_MODERATE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_RESTRICTED
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_SAFE
     * @return The safety-level
     */
    public String getSafetyLevel() {
        return safetyLevel;
    }

    /**
     * Set the safety level (adultness) of a photo.
     * <p>
     * 
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_MODERATE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_RESTRICTED
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_SAFE
     * @param safetyLevel
     */
    public void setSafetyLevel(String safetyLevel) {
        this.safetyLevel = safetyLevel;
    }

    public boolean isAsync() {
        return async;
    }

    /**
     * Switch the Uploader behaviour - sychronous or asyncrounous.
     * <p>
     * 
     * The default is sychronous.
     * 
     * @param async
     *            boolean
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

}
