/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.reflection.Argument;
import com.flickr4java.flickr.reflection.Error;
import com.flickr4java.flickr.reflection.Method;
import com.flickr4java.flickr.reflection.ReflectionInterface;

/**
 * @author Anthony Eden
 */
public class ReflectionInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetMethodInfo_public() throws FlickrException {
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
        Collection<Argument> args = method.getArguments();
        Iterator<Argument> argsIterator = args.iterator();

        Argument api_key = argsIterator.next();
        assertEquals(Flickr.API_KEY, api_key.getName());
        assertFalse(api_key.isOptional());
        assertNotNull(api_key.getDescription());

        Argument date = argsIterator.next();
        assertEquals("date", date.getName());
        assertTrue(date.isOptional());

        Argument panda = argsIterator.next();
        assertEquals("use_panda", panda.getName());
        assertTrue(panda.isOptional());

        Argument extras = argsIterator.next();
        assertEquals("extras", extras.getName());
        assertTrue(extras.isOptional());

        Argument per_page = argsIterator.next();
        assertEquals("per_page", per_page.getName());
        assertTrue(per_page.isOptional());

        Argument page = argsIterator.next();
        assertEquals("page", page.getName());
        assertTrue(page.isOptional());

        Collection<Error> errors = method.getErrors();
        assertNotNull(errors);
        assertTrue(!errors.isEmpty());
        Iterator<Error> errorsIterator = errors.iterator();
        Error error = errorsIterator.next();
        assertNotNull(error);
        assertTrue(error.getCode() > 0);
        assertNotNull(error.getMessage());
        assertNotNull(error.getExplaination());
    }

    @Test
    public void testGetMethodInfo_withPerms() throws FlickrException {
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

        Collection<Argument> c = method.getArguments();
        Iterator<Argument> argsIterator = c.iterator();

        Argument api_key = argsIterator.next();
        assertEquals(Flickr.API_KEY, api_key.getName());
        assertFalse(api_key.isOptional());
        assertNotNull(api_key.getDescription());

        Argument photo_id = argsIterator.next();
        assertEquals("photo_id", photo_id.getName());
        assertFalse(photo_id.isOptional());

        Argument tags = argsIterator.next();
        assertEquals("tags", tags.getName());
        assertFalse(tags.isOptional());

    }

    @Test
    public void testGetMethods() throws FlickrException {
        ReflectionInterface reflectionInterface = flickr.getReflectionInterface();
        Collection<String> methods = reflectionInterface.getMethods();
        assertNotNull(methods);
        assertTrue("There are no methods in the method list", methods.size() > 0);
        Iterator<String> methodsIterator = methods.iterator();
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
