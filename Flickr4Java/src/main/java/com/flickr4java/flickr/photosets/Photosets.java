/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photosets;

import java.util.Collection;

/**
 * Photoset collection. This class is required instead of a basic Collection object because of the cancreate flag.
 * 
 * @author Anthony Eden
 */
public class Photosets {

    private boolean canCreate;

    private Collection<Photoset> photosets;

    public Photosets() {

    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public Collection<Photoset> getPhotosets() {
        return photosets;
    }

    public void setPhotosets(Collection<Photoset> photosets) {
        this.photosets = photosets;
    }

}
