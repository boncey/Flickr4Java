package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Matt Ray
 */
public class TestInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;

            SOAP soap = new SOAP(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), soap);

        } finally {
            IOUtilities.close(in);
        }
    }

    public void testEcho() throws FlickrException, IOException, SAXException {
        TestInterface iface = flickr.getTestInterface();
        Map<String, String> params = new HashMap<String, String>();
        params.put("test", "test");
        Collection results = iface.echo(params);
        assertNotNull(results);
    }

    public void testLogin() throws FlickrException, IOException, SAXException {
        RequestContext requestContext = RequestContext.getRequestContext();
        TestInterface iface = flickr.getTestInterface();
        User user = iface.login();
        assertNotNull(user);
    }

}
