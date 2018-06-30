package com.flickr4java.flickr.people;

public class PhotoLimits {
	

	private String maxDisplay;
	private String maxUpload;
	
	/**
     * Maximum photo display pixels
     * 
     * @return String max pixels
     */
	
	public String getMaxDisplay(){
		return maxDisplay;
	}
	
	public void setMaxDisplay(String maxDisplay){
		this.maxDisplay = maxDisplay;
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
