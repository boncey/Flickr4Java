/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

import com.flickr4java.flickr.util.StringUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

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
        if (comment != null)
            setComment(Integer.parseInt(comment));
    }

    public int getAddmeta() {
        return addmeta;
    }

    public void setAddmeta(int addmeta) {
        this.addmeta = addmeta;
    }

    public void setAddmeta(String addmeta) {
        if (addmeta != null)
            setAddmeta(Integer.parseInt(addmeta));
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        // object must be Permissions at this point
        Permissions test = (Permissions) obj;
        Class cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                try {
                    Object res = method[i].invoke(this);
                    Object resTest = method[i].invoke(test);
                    String retType = method[i].getReturnType().toString();
                    if (retType.indexOf("class") == 0) {
                        if (res != null && resTest != null) {
                            if (!res.equals(resTest))
                                return false;
                        } else {
                            // return false;
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer) resTest)))
                            return false;
                    } else if (retType.equals("boolean")) {
                        if (!((Boolean) res).equals(((Boolean) resTest)))
                            return false;
                    } else {
                        System.out.println(method[i].getName() + "|" + method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    // System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
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
