/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.reflection;

/**
 * Describes an argument of a Flickr-method.
 * 
 * @author Anthony Eden
 * @see Method#getArguments()
 * @see Method#setArguments(java.util.Collection)
 * @version $Id: Argument.java,v 1.3 2007/11/18 22:48:09 x-mago Exp $
 */
public class Argument {

    private String name;

    private boolean optional;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
