/**
 * @author acaplan
 */
package com.flickr4java.flickr.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;

/**
 * @author acaplan
 *
 */
public class Flickr4JavaTest {

    protected Flickr flickr = null;
    protected TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        //Flickr.debugStream = true;

        testProperties = new TestProperties();

        REST rest = new REST();
        rest.setHost(testProperties.getHost());

        flickr = new Flickr(
                testProperties.getApiKey(),
                testProperties.getSecret(),
                rest
                );

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }
}
