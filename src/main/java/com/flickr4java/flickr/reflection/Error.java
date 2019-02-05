

package com.flickr4java.flickr.reflection;

/**
 * Describes one possible error-code of a Flickr-method.
 * 
 * @author Anthony Eden
 * @see Method#getErrors()
 * @see Method#setErrors(java.util.Collection)
 * @version $Id: Error.java,v 1.3 2007/11/18 22:48:09 x-mago Exp $
 */
public class Error {

    private int code;

    private String message;

    private String explaination;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCode(String code) {
        setCode(Integer.parseInt(code));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExplaination() {
        return explaination;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
    }

}
