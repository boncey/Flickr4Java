package com.flickr4java.flickr.people;

public class VideoLimits {

	private String maxDuration;
	private String maxUpload;
	
	/**
     * Maximum photo display pixels
     * 
     * @return String max pixels
     */
	
	public String getMaxDuration(){
		return maxDuration;
	}
	
	public void setMaxDuration(String maxDuration){
		this.maxDuration = maxDuration;
	}
	
	 /**
     * Maximum photo upload
     * 
     * @return String upload capacity
     */
	
	
	public String getMaxUpload(){
		return maxUpload;
	}
	
	public void setMaxUpload(String maxUpload){
		this.maxUpload = maxUpload;
	}
	
}
