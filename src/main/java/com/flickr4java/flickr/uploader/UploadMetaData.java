/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.uploader;

import com.flickr4java.flickr.util.StringUtilities;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Metadata that describe a photo.
 * 
 * @author Anthony Eden
 * @version $Id: UploadMetaData.java,v 1.7 2007/11/02 21:46:52 x-mago Exp $
 */
public class UploadMetaData {

	private String filename = "image.jpg";
	
	private String fileMimeType = "image/jpeg";
	
    private String title;

    private String description;

    private Collection<String> tags;

    private boolean publicFlag;

    private boolean friendFlag;

    private boolean familyFlag;

    private boolean async;

    private Boolean hidden;

    private String safetyLevel;

    private String contentType;

    private String photoId;

    public String getTitle() {
        return title;
    }

    public UploadMetaData setTitle(String title) {
        this.title = title;

        return this;
    }

    public String getDescription() {
        return description;
    }

    public UploadMetaData setDescription(String description) {
        this.description = description;

        return this;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public UploadMetaData setTags(Collection<String> tags) {
        this.tags = tags;

        return this;
    }

    public boolean isPublicFlag() {
        return publicFlag;
    }

    public UploadMetaData setPublicFlag(boolean publicFlag) {
        this.publicFlag = publicFlag;

        return this;
    }

    public boolean isFriendFlag() {
        return friendFlag;
    }

    public UploadMetaData setFriendFlag(boolean friendFlag) {
        this.friendFlag = friendFlag;

        return this;
    }

    public boolean isFamilyFlag() {
        return familyFlag;
    }

    public UploadMetaData setFamilyFlag(boolean familyFlag) {
        this.familyFlag = familyFlag;

        return this;
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
    public UploadMetaData setContentType(String contentType) {
        this.contentType = contentType;

        return this;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public UploadMetaData setHidden(Boolean hidden) {
        this.hidden = hidden;

        return this;
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
    public UploadMetaData setSafetyLevel(String safetyLevel) {
        this.safetyLevel = safetyLevel;

        return this;
    }

    /**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public UploadMetaData setFilename(String filename) {
		this.filename = filename;
		
		return this;
	}

	public String getFilemimetype() {
		return fileMimeType;
	}

	public void setFilemimetype(String fileMimeType) {
		this.fileMimeType = fileMimeType;
	}

	public boolean isAsync() {
        return async;
    }

    /**
     * Switch the Uploader behaviour - synchronous or asynchronous.
     * <p>
     * 
     * The default is sychronous.
     * 
     * @param async
     *            boolean
     */
    public UploadMetaData setAsync(boolean async) {
        this.async = async;

        return this;
    }

    public String getPhotoId() {
        return photoId;
    }

    /**
     * Set the existing photo id for a replace operation.
     * @param photoId
     */
    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    /**
     * Get the upload parameters.
     * @return
     */
    public Map<String, String> getUploadParameters() {
        Map<String, String> parameters = new TreeMap<>();


        String title = getTitle();
        if (title != null) {
            parameters.put("title", title);
        }

        String description = getDescription();
        if (description != null) {
            parameters.put("description", description);
        }

        Collection<String> tags = getTags();
        if (tags != null) {
            parameters.put("tags", StringUtilities.join(tags, " "));
        }

        if (isHidden() != null) {
            parameters.put("hidden", isHidden().booleanValue() ? "1" : "0");
        }

        if (getSafetyLevel() != null) {
            parameters.put("safety_level", getSafetyLevel());
        }

        if (getContentType() != null) {
            parameters.put("content_type", getContentType());
        }

        if (getPhotoId() != null) {
            parameters.put("photo_id", getPhotoId());
        }

        parameters.put("is_public", isPublicFlag() ? "1" : "0");
        parameters.put("is_family", isFamilyFlag() ? "1" : "0");
        parameters.put("is_friend", isFriendFlag() ? "1" : "0");
        parameters.put("async", isAsync() ? "1" : "0");

        return parameters;
    }

    public static UploadMetaData replace(boolean async, String photoId) {
        UploadMetaData metaData = new UploadMetaData();
        metaData.async = async;
        metaData.photoId = photoId;
        return metaData;
    }

}
