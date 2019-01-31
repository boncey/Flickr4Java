package com.flickr4java.flickr;

/**
 * {@link RuntimeException} wrapper for the various XML or IO Exceptions thrown.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class FlickrRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1303207981175254196L;

    private String errorCode;

    private String errorMessage;

    public FlickrRuntimeException() {
    }

    public FlickrRuntimeException(String message) {
        super(message);
    }

    public FlickrRuntimeException(Throwable rootCause) {
        super(rootCause);
    }

    public FlickrRuntimeException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public FlickrRuntimeException(String errorCode, String errorMessage) {
        super(errorCode + ": " + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
