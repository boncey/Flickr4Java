/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

/**
 * @author Anthony Eden
 */
public class Permissions {

    private String id;

    private boolean publicFlag;

    private boolean friendFlag;

    private boolean familyFlag;

    private int comment = 0;

    private int addmeta = 0;

    public Permissions() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(boolean publicFlag) {
        this.publicFlag = publicFlag;
    }

    public boolean isFriendFlag() {
        return friendFlag;
    }

    public void setFriendFlag(boolean friendFlag) {
        this.friendFlag = friendFlag;
    }

    public boolean isFamilyFlag() {
        return familyFlag;
    }

    public void setFamilyFlag(boolean familyFlag) {
        this.familyFlag = familyFlag;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setComment(String comment) {
        if (comment != null) {
            setComment(Integer.parseInt(comment));
        }
    }

    public int getAddmeta() {
        return addmeta;
    }

    public void setAddmeta(int addmeta) {
        this.addmeta = addmeta;
    }

    public void setAddmeta(String addmeta) {
        if (addmeta != null) {
            setAddmeta(Integer.parseInt(addmeta));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Permissions test = (Permissions) obj;
        // id seems to be photo id
        if (id == null ? test.id == null : id.equals(test.id)) {
            return publicFlag == test.publicFlag && friendFlag == test.friendFlag && familyFlag == test.familyFlag && comment == test.comment
                    && addmeta == test.addmeta;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 87;
        hash += id.hashCode();
        hash += new Integer(comment).hashCode();
        hash += new Integer(addmeta).hashCode();
        hash += new Boolean(publicFlag).hashCode();
        hash += new Boolean(friendFlag).hashCode();
        hash += new Boolean(familyFlag).hashCode();
        return hash;
    }
}
