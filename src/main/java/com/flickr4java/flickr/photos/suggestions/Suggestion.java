package com.flickr4java.flickr.photos.suggestions;

import com.flickr4java.flickr.places.Location;

public class Suggestion {
	
	private String suggestionId;
	private String photoId;
	private String dateSuggested;
	private String suggestorId;
	private String suggestorUsername;
	private String note;
	private Location location;
	
	public String getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(String suggestionId) {
		this.suggestionId = suggestionId;
	}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getDateSuggested() {
		return dateSuggested;
	}
	public void setDateSuggested(String dateSuggested) {
		this.dateSuggested = dateSuggested;
	}
	public String getSuggestorId() {
		return suggestorId;
	}
	public void setSuggestorId(String suggestorId) {
		this.suggestorId = suggestorId;
	}
	public String getSuggestorUsername() {
		return suggestorUsername;
	}
	public void setSuggestorUsername(String suggestorUsername) {
		this.suggestorUsername = suggestorUsername;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

}
