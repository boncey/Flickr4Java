package com.flickr4java.flickr.photos;

public class PhotoSet {
	
	 private String title;
	 private String id;
	 private String primary;
	 private String secret;
	 private String server;
	 private String farm;
	 private int viewCount;
	 private int commentCount;
	 private int countPhoto;
	 private int countVideo;
	 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
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
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getCountPhoto() {
		return countPhoto;
	}
	public void setCountPhoto(int countPhoto) {
		this.countPhoto = countPhoto;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getCountVideo() {
		return countVideo;
	}
	public void setCountVideo(int countVideo) {
		this.countVideo = countVideo;
	}


}
