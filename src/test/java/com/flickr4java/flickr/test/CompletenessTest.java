package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.reflection.ReflectionInterface;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Properties;

/**
 * Tests the basic completeness of the api.
 * 
 * @author till (Till Krech) flickr:extranoise
 * 
 */
public class CompletenessTest extends Flickr4JavaTest {

    Properties replacements;

    @Override
    @Before
    public void setUp() throws FlickrException {
        super.setUp();
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/completenesstest.properties");
            replacements = new Properties();
            replacements.load(in);
        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            IOUtilities.close(in);
        }

    }

    // Test disabled in normal circumstances - just enable when you want to test completeness
    @Ignore
    @Test
    public void testIfComplete() throws FlickrException {
        ReflectionInterface ri = flickr.getReflectionInterface();
        Iterator<String> mit = ri.getMethods().iterator();
        int notFound = 0;
        while (mit.hasNext()) {
            String method = mit.next();
            if (!checkMethod(method)) {
                notFound++;
            }
        }
        assertEquals(0, notFound);
    }

    private boolean checkMethod(String fullMethodName) {
        String repl = getReplacement(fullMethodName);
        String methodName;
        String fqClassName;
        if (repl != null) {
            if ("skip".equals(repl)) {
                return true;
            }
            fqClassName = repl.substring(0, repl.lastIndexOf('.'));
            methodName = repl.substring(repl.lastIndexOf('.') + 1);
        } else {
            int dotIdx = fullMethodName.lastIndexOf('.');
            String pack = fullMethodName.substring(0, dotIdx);
            methodName = fullMethodName.substring(dotIdx + 1);
            dotIdx = pack.lastIndexOf('.');
            String candidate = pack.substring(dotIdx + 1);
            String javaPack = "com.flickr4java." + pack;
            String className = Character.toUpperCase(candidate.charAt(0)) + candidate.substring(1) + "Interface";
            fqClassName = javaPack + "." + className;
        }
        boolean found = false;
        try {
            Class<?> cl = Class.forName(fqClassName);
            Method[] javaMethods = cl.getMethods();
            for (Method javaMethod : javaMethods) {
                if (javaMethod.getName().equals(methodName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("ATTENTION: Method not implemented in Flickr4Java: " + fqClassName + "." + methodName);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ATTENTION:  Class not implemented in Flickr4Java: [" + fqClassName + "] (Method: " + methodName + ")");
        }
        return found;
    }

    private String getReplacement(String fullMethodName) {
        String repl = replacements.getProperty(fullMethodName);
        if (repl == null) {
            String meth = fullMethodName.substring(fullMethodName.lastIndexOf('.') + 1);
            @SuppressWarnings("rawtypes")
            Iterator keys = replacements.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (key.endsWith(".*")) {
                    String keyPack = key.substring(0, key.length() - 2);
                    String methPack = fullMethodName.substring(0, fullMethodName.lastIndexOf('.'));
                    if (keyPack.equals(methPack)) {
                        String cls = replacements.getProperty(key);
                        if (cls.endsWith(".*")) {
                            cls = cls.substring(0, cls.length() - 2);
                        }
                        repl = cls + "." + meth;
                        break;
                    }
                }
            }
        }
        return repl;
    }
}
