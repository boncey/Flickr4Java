/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

/**
 * @author Anthony Eden
 */
public class Editability {

    private boolean comment;

    private boolean addmeta;

    public Editability() {

    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public boolean isAddmeta() {
        return addmeta;
    }

    public void setAddmeta(boolean addmeta) {
        this.addmeta = addmeta;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Editability test = (Editability) obj;
        return comment == test.comment && addmeta == test.addmeta;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash += new Boolean(comment).hashCode();
        hash += new Boolean(addmeta).hashCode();
        return hash;
    }

}
