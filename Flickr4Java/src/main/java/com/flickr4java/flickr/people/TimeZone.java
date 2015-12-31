package com.flickr4java.flickr.people;

public class TimeZone {

    private String label;

    private String offset;

    private String timeZoneId;

    /**
     * Time Zone text
     * 
     * @return the label
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
    public String getOffset() {
        return offset;
    }

    /**
     * @deprecated typo in method name, use {@link #getOffset()}.
     */
    @Deprecated
    public String geOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    /**
     * Time Zone id
     * 
     * @return timezone_id offset value
     */
    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String id) {
        this.timeZoneId = id;
    }

}
