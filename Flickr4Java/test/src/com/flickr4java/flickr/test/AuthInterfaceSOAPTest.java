/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.test;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;

/**
 * @author Anthony Eden
 */
public class AuthInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() {
        testProperties = new TestProperties();

        Flickr.debugRequest = true;
        Flickr.debugStream = true;
        SOAP soap = new SOAP();
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Ignore
    @Test
    public void testDoNothing() {

    }
}
