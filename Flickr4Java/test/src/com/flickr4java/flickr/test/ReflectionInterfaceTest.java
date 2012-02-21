/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.reflection.Argument;
import com.flickr4java.flickr.reflection.Error;
import com.flickr4java.flickr.reflection.Method;
import com.flickr4java.flickr.reflection.ReflectionInterface;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Anthony Eden
 */
public class ReflectionInterfaceTest {

    Flickr flickr = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        testProperties = new TestProperties();

        REST rest = new REST();
        flickr = new Flickr(testProperties.getApiKey(), testProperties.getSecret(), rest);

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testGetMethodInfo_public() throws FlickrException, IOException, SAXException {
        String methodName = "flickr.interestingness.getList";
        ReflectionInterface reflectionInterface = flickr.getReflectionInterface();
        Method method = reflectionInterface.getMethodInfo(methodName);
        assertNotNull(method);
        assertEquals(methodName, method.getName());
        assertFalse(method.needsSigning());
        assertEquals(0, method.getRequiredPerms());
        assertFalse(method.needsLogin());

        assertNotNull(method.getArguments());
        assertEquals(6, method.getArguments().size());
        Iterator argsIterator = method.getArguments().iterator();

        Argument api_key = (Argument) argsIterator.next();
        assertEquals(Flickr.API_KEY, api_key.getName());
        assertFalse(api_key.isOptional());
        assertNotNull(api_key.getDescription());

        Argument date = (Argument) argsIterator.next();
        assertEquals("date", date.getName());
        assertTrue(date.isOptional());

        Argument panda = (Argument) argsIterator.next();
        assertEquals("use_panda", panda.getName());
        assertTrue(panda.isOptional());

        Argument extras = (Argument) argsIterator.next();
        assertEquals("extras", extras.getName());
        assertTrue(extras.isOptional());

        Argument per_page = (Argument) argsIterator.next();
        assertEquals("per_page", per_page.getName());
        assertTrue(per_page.isOptional());

        Argument page = (Argument) argsIterator.next();
        assertEquals("page", page.getName());
        assertTrue(page.isOptional());

        Collection errors = method.getErrors();
        assertNotNull(errors);
        assertTrue(errors.size() > 0);
        Iterator errorsIterator = errors.iterator();
        Error error = (Error) errorsIterator.next();
        assertNotNull(error);
        assertTrue(error.getCode() > 0);
        assertNotNull(error.getMessage());
        assertNotNull(error.getExplaination());
    }

    @Test
    public void testGetMethodInfo_withPerms() throws FlickrException, IOException, SAXException {
        String methodName = "flickr.photos.addTags";
        ReflectionInterface reflectionInterface = flickr.getReflectionInterface();
        Method method = reflectionInterface.getMethodInfo(methodName);
        assertNotNull(method);
        assertEquals(methodName, method.getName());
        assertNotNull(method.getArguments());
        assertEquals(3, method.getArguments().size());

        assertTrue(method.needsSigning());
        assertTrue(method.needsLogin());
        assertEquals(Method.WRITE_PERMISSION, method.getRequiredPerms());

        Iterator argsIterator = method.getArguments().iterator();

        Argument api_key = (Argument) argsIterator.next();
        assertEquals(Flickr.API_KEY, api_key.getName());
        assertFalse(api_key.isOptional());
        assertNotNull(api_key.getDescription());

        Argument photo_id = (Argument) argsIterator.next();
        assertEquals("photo_id", photo_id.getName());
        assertFalse(photo_id.isOptional());

        Argument tags = (Argument) argsIterator.next();
        assertEquals("tags", tags.getName());
        assertFalse(tags.isOptional());

    }

    @Test
    public void testGetMethods() throws FlickrException, IOException, SAXException {
        ReflectionInterface reflectionInterface = flickr.getReflectionInterface();
        Collection methods = reflectionInterface.getMethods();
        assertNotNull(methods);
        assertTrue("There are no methods in the method list", methods.size() > 0);
        Iterator methodsIterator = methods.iterator();
        boolean foundAddTags = false;
        boolean foundGetLocation = false;

        while (methodsIterator.hasNext()) {
            String methodName = (String) methodsIterator.next();
            if ("flickr.photos.addTags".equals(methodName)) {
                foundAddTags = true;
            }
            if ("flickr.photos.geo.getLocation".equals(methodName)) {
                foundGetLocation = true;
            }
        }
        assertTrue("Method \"flickr.photos.addTags\" not found", foundAddTags);
        assertTrue("Method \"flickr.photos.geo.getLocation\" not found", foundGetLocation);
    }

}
