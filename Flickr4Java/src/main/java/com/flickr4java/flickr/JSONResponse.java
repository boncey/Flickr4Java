package com.flickr4java.flickr;

/**
 * An interface for a response object which carries json
 */
public interface JSONResponse {

    /**
     * Returns json as String
     */
    StringBuilder getBody();

}
