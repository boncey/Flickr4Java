/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.auth;

import com.flickr4java.flickr.people.User;

import java.io.Serializable;

/**
 * @author Anthony Eden
 */
public class Auth implements Serializable {

    private static final long serialVersionUID = -2254618470673679663L;

    private String token;

    private String tokenSecret;

    private Permission permission;

    private User user;

    public Auth() {

    }

    public Auth(Permission permission, User user) {

        super();
        this.permission = permission;
        this.user = user;
    }

    /**
     * @return the tokenSecret
     */
    public String getTokenSecret() {
        return tokenSecret;
    }

    /**
     * @param tokenSecret
     *            the tokenSecret to set
     */
    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
