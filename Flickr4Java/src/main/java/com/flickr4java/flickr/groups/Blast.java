package com.flickr4java.flickr.groups;

public class Blast {

		private String dateBlastAdded;

	    private String userId;

	    private String blast;

	    /**
	     * Unix timestamp formatted date
	     * 
	     * @return date blast was added
	     */
	    public String getDateBlastAdded() {
	        return dateBlastAdded;
	    }

	    public void setDateBlastAdded(String dateBlastAdded) {
	        this.dateBlastAdded = dateBlastAdded;
	    }

	    /**
	     * Blaster's user_id
	     * 
	     * @return user_id
	     */
	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    /**
	     * Text of the blast
	     * 
	     * @return blast text
	     */
	    public String getBlast() {
	        return blast;
	    }

	    public void setBlast(String blast) {
	        this.blast = blast;
	    }
	
}
