package com.flickr4java.flickr.groups;

public class Restriction {

	    private Boolean photosOk;

	    private Boolean videosOk;
	    
	    private Boolean imagesOk;

	    private Boolean screensOk;
	    
	    private Boolean artOk;
	    
	    private Boolean safeOk;
	    
	    private Boolean moderateOk;
	    
	    private Boolean restrictedOk;
	    
	    private Boolean hasGeo;

	    /**
	     * 
	     *
	     * @return Are Photos allowed in the group
	     */
	    public Boolean isPhotosOk() {
	        return photosOk;
	    }
	    
	    public void setIsPhotosOk(Boolean value) {
	    	this.photosOk = value;
	    }

	    /**
	     * 
	     * 
	     * @return  Are Videos allowed in the group
	     */
	    public Boolean isVideosOk() {
	        return videosOk;
	    }
	    
	    public void setIsVideosOk(Boolean value) {
	    	this.videosOk = value;
	    	
	    }

	    /**
	     * 
	     * 
	     * @return  Are Images allowed in the group
	     */
	    public Boolean isImagesOk() {
	        return imagesOk;
	    }
	    
	    public void setIsImagesOk(Boolean value) {
	    	this.imagesOk = value;
	    }
	    /**
	     * 
	     * @return Are Screens allowed in the group
	     */
	    public Boolean isScreensOk() {
	        return screensOk;
	    }
	    
	    public void setIsScreensOk(Boolean value) {
	    	this.screensOk = value;
	    }

	    /**
	     * 
	     * @return Are art allowed in the group
	     */
	    public Boolean isArtOk() {
	        return artOk;
	    }
	    
	    public void setIsArtOk(Boolean value) {
	    	this.artOk = value;
	    }

	    /**
	     * 
	     * @return Are Safe rated images allowed in the group
	     */
	    public Boolean isSafeOk() {
	        return safeOk;
	    }
	    
	    public void setIsSafeOk(Boolean value) {
	    	this.safeOk = value;
	    }
	    /**
	     * 
	     * @return Are Moderate rated images allowed in the group
	     */
	    public Boolean isModerateOk() {
	        return moderateOk;
	    }
	    
	    public void setIsModerateOk(Boolean value) {
	    	this.moderateOk = value;
	    }

	    /**
	     * 
	     * @return Are Restricted rated images allowed in the group
	     */
	    public Boolean isRestrictedOk() {
	        return restrictedOk;
	    }
	    
	    public void setIsRestrictedOk(Boolean value) {
	    	this.restrictedOk = value;
	    }

	    /**
	     * 
	     * @return Are Geo-tagged images allowed in the group
	     */
	    public Boolean isHasGeo() {
	        return hasGeo;
	    }
	    
	    public void setIsHasGeo(Boolean value) {
	    	this.hasGeo = value;
	    }
	    

}
