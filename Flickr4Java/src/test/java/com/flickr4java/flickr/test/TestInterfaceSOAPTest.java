package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.people.User;

/**
 * @author Matt Ray
 */
public class TestInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;

        SOAP soap = new SOAP(testProperties.getHost());

        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

    }

    @Ignore
    @Test
    public void testEcho() throws FlickrException {
        TestInterface iface = flickr.getTestInterface();
        Map<String, String> params = new HashMap<String, String>();
        params.put("test", "test");
        Collection results = iface.echo(params);
        assertNotNull(results);
    }
    @Ignore
    @Test
    public void testLogin() throws FlickrException {
        TestInterface iface = flickr.getTestInterface();
        User user = iface.login();
        assertNotNull(user);
    }

}
