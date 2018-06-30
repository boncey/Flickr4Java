package com.flickr4java.flickr.photos.upload;

/**
 * Photo upload ticket.
 * <p>
 * The ticketId attribute contains the ticket id.<br>
 * If the ticket wasn't found, the invalid attribute is set.<br>
 * The status of the ticket is passed in the status attribute;
 * <p>
 * 0 means not completed,<br>
 * 1 means completed and<br>
 * 2 means the ticket failed (indicating there was a problem converting the file).
 * <p>
 * When the status is 1, the photo id is passed in the photoid attribute. The photo id can then be used as with the synchronous upload API.
 * 
 * @author till (Till Krech) extranoise:flickr
 * @version $Id: Ticket.java,v 1.2 2007/11/02 21:46:52 x-mago Exp $
 */
public class Ticket {
    public static final int UNCOMPLETED = 0;

    public static final int COMPLETED = 1;

    public static final int FAILED = 2;

    private String ticketId;

    private boolean invalid;

    private String photoId;

    private int status;

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int complete) {
        this.status = complete;
    }

    public boolean hasCompleted() {
        return status == COMPLETED;
    }

    public boolean hasFailed() {
        return status == FAILED;
    }

    public boolean isBusy() {
        return status == UNCOMPLETED;
    }

}
