package com.flickr4java.flickr.people;

public class TimeZone {

	private String label;

    private String offset;

    /**
     * Time Zone text
     * 
     * @return timezone
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Time Zone offset
     * 
     * @return timezone offset value
     */
    public String geOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
	
}
