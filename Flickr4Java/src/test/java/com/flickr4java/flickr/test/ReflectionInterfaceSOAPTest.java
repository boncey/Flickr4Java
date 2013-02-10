/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.reflection.Method;
import com.flickr4java.flickr.reflection.ReflectionInterface;

/**
 * @author Anthony Eden
 */
public class ReflectionInterfaceSOAPTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException {
        testProperties = new TestProperties();

        SOAP soap = new SOAP(testProperties.getHost());
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), soap);

    }

    @Ignore
    @Test
    public void testGetMethodInfo() throws FlickrException {
        String methodName = "flickr.urls.lookupGroup";
        ReflectionInterface reflectionInterface = flickr.getReflectionInterface();
        Method method = reflectionInterface.getMethodInfo(methodName);
        assertNotNull(method);
        assertEquals(methodName, method.getName());
    }

    @Ignore
    @Test
    public void testGetMethods() throws FlickrException {
        ReflectionInterface reflectionInterface = flickr.getReflectionInterface();
        Collection<String> methods = reflectionInterface.getMethods();
        assertNotNull(methods);
        assertTrue("There are no methods in the method list", methods.size() > 0);
    }

}
