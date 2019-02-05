
package com.flickr4java.flickr.photosets;

import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;

/**
 * Meta information about a photoset. To retrieve the photos in the photoset use PhotosetsInterface.getPhotos().
 * 
 * @author Anthony Eden
 * @version $Id: Photoset.java,v 1.7 2009/07/12 22:43:07 x-mago Exp $
 */
public class Photoset {
    private static final long serialVersionUID = 12L;

    private String id;

    private String url;

    private User owner;

    private Photo primaryPhoto;

    private String secret;

    private String server;

    private String farm;

    private int photoCount;
    
    private int videoCount;
    
    private int viewCount;
    
    private int commentCount;
    
    private String dateCreate;
    
    private String dateUpdate;

    private String title;

    private String description;
    
    private boolean isVisible;
    
    private boolean canComment;
    
    private boolean needsInterstitial;

    public Photoset() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        if (url == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("https://www.flickr.com/photos/");
            sb.append(getOwner().getId());
            sb.append("/sets/");
            sb.append(getId());
            sb.append("/");
            return sb.toString();
        } else {
            return url;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Photo getPrimaryPhoto() {
        return primaryPhoto;
    }

    public void setPrimaryPhoto(Photo primaryPhoto) {
        this.primaryPhoto = primaryPhoto;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public void setPhotoCount(String photoCount) {
        if (photoCount != null) {
            setPhotoCount(Integer.parseInt(photoCount));
        }
    }

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

	public int getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(String dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}



	public boolean isNeedsInterstitial() {
		return needsInterstitial;
	}

	public void setIsNeedsInterstitial(boolean needsInterstitial) {
		this.needsInterstitial = needsInterstitial;
	}

	public boolean isCanComment() {
		return canComment;
	}

	public void setIsCanComment(boolean canComment) {
		this.canComment = canComment;
	}

}
