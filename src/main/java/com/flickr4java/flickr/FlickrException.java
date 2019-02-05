
package com.flickr4java.flickr;

/**
 * Exception which wraps a Flickr error.
 * 
 * @author Anthony Eden
 */
public class FlickrException extends Exception {

    private static final long serialVersionUID = 7958091410349084831L;

    private String errorCode;

    private String errorMessage;

    public FlickrException(String errorMessage) {

        super(errorMessage);
    }

    public FlickrException(String errorMessage, Throwable rootCause) {

        super(errorMessage, rootCause);
    }

    public FlickrException(Throwable rootCause) {

        super(rootCause);
    }

    public FlickrException(String errorCode, String errorMessage) {
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
