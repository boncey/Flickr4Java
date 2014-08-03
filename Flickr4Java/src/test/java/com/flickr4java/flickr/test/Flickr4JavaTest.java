/**
 * @author acaplan
 */
package com.flickr4java.flickr.test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;

import org.junit.Before;

/**
 * @author acaplan
 * 
 */
public class Flickr4JavaTest {

    protected Flickr flickr = null;

    protected TestProperties testProperties;

    /**
     * @throws FlickrException
     */
    @Before
    public void setUp() throws FlickrException {
        testProperties = new TestProperties();

        REST rest = new REST();
        rest.setHost(testProperties.getHost());

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

        setAuth(Permission.READ);
    }

    /**
     * Set auth parameters for API calls that need it.
     * 
     * @param perms
     */
    protected void setAuth(Permission perms) {
        Auth auth = new Auth();
        auth.setPermission(perms);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    /**
     * Certain tests don't require authorization and calling with auth set may mask other errors.
     */
    protected void clearAuth() {
        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(null);
    }

}
