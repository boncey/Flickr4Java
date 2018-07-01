/**
 * @author acaplan
 */
package com.flickr4java.flickr.test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.IFlickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.test.util.FlickrStub;
import com.flickr4java.flickr.test.util.TestProperties;
import com.flickr4java.flickr.test.util.TestPropertiesFactory;
import org.junit.Before;

/**
 * @author acaplan
 * 
 */
public class Flickr4JavaTest {

    protected IFlickr flickr;

    protected TestProperties testProperties;

    /**
     * @throws FlickrException
     */
    @Before
    public void setUp() throws FlickrException {
        testProperties = TestPropertiesFactory.getTestProperties();

        if (testProperties.isRealFlickr()) {
            REST rest = new REST();
            rest.setHost(testProperties.getHost());

            flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

            setAuth(Permission.READ);
        } else {
            flickr = new FlickrStub();
        }
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
