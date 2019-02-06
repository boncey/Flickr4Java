
package com.flickr4java.flickr;

/**
 * Class encapsulating a name/value parameter.
 * 
 * @author Anthony Eden
 */
public class Parameter {

    private String name;

    private Object value;

    /**
     * Construct the Parameter.
     * 
     * @param name
     *            The parameter name
     * @param value
     *            The parameter value
     */
    public Parameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Parameter(String name, long value) {
        this.name = name;
        this.value = new Long(value);
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

}
