/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.util.ByteUtilities;

/**
 * Utilities used by the authentication API.
 *
 * @author Anthony Eden
 * @version $Id: AuthUtilities.java,v 1.11 2009/11/07 23:23:24 x-mago Exp $
 */
public class AuthUtilities {

    public static String getMultipartSignature(String sharedSecret, Map<String, String> params) {
        List ignoreParameters = new ArrayList();
        ignoreParameters.add("photo");

        addAuthToken(params);

        StringBuffer buffer = new StringBuffer();
        buffer.append(sharedSecret);
        //        @TODO - sort

        //Collections.sort(params, new ParameterAlphaComparator());
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if (!ignoreParameters.contains(entry.getKey().toLowerCase())) {
                buffer.append(entry.getKey());
                buffer.append(entry.getValue());
            }
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return ByteUtilities.toHexString(md.digest(buffer.toString().getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException u) {
            throw new RuntimeException(u);
        }
    }

    /**
     * Adds the auth_token to the parameter list if it is necessary.
     * @param params
     */
    public static void addAuthToken(Map<String, String> params) {
        //Checking for the auth_token parameter
        if (!params.containsKey("auth_token")) {
            if (RequestContext.getRequestContext().getAuth() != null) {
                String authToken = RequestContext.getRequestContext().getAuth().getToken();
                if(authToken != null && !authToken.equals(""))
                    params.put("auth_token", authToken);
            }
        }
    }

    /**
     * Check, if we are authenticated.
     *
     * @param params
     * @return isAuthenticated
     */
    public static boolean isAuthenticated(Map<String, String> params) {
        if (!params.containsKey("auth_token")) {
            if (RequestContext.getRequestContext().getAuth() != null) {
                String authToken = RequestContext.getRequestContext().getAuth().getToken();
                if (authToken != null && !authToken.equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
}
