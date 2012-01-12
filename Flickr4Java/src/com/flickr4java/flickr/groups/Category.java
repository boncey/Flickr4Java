/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.groups;

import java.util.Collection;

/**
 * Class representing a group Category.
 *
 * @author Anthony Eden
 */
public class Category {
	private static final long serialVersionUID = 12L;

    private String name;
    private String path;
    private String pathIds;
    private Collection subcategories;
    private Collection groups;

    public Category() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathIds() {
        return pathIds;
    }

    public void setPathIds(String pathIds) {
        this.pathIds = pathIds;
    }

    public Collection getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Collection subcategories) {
        this.subcategories = subcategories;
    }

    public Collection getGroups() {
        return groups;
    }

    public void setGroups(Collection groups) {
        this.groups = groups;
    }

}
