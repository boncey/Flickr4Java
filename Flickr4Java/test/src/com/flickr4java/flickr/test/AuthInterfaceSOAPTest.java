/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Anthony Eden
 */
public class AuthInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    @Override
    public void setUp() throws ParserConfigurationException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugRequest = true;
            Flickr.debugStream = true;
            OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
                    .apiSecret(properties.getProperty("secret")).build();
            SOAP soap = new SOAP(service);
            flickr = new Flickr(properties.getProperty("apiKey"), soap);

            Auth auth = new Auth();
            auth.setPermission(Permission.READ);
            auth.setToken(properties.getProperty("token"));
            auth.setTokenSecret(properties.getProperty("tokensecret"));

            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setAuth(auth);
            flickr.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testDoNothing() {

    }
}
