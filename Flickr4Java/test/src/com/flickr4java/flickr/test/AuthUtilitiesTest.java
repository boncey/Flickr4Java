package com.flickr4java.flickr.test;

import junit.framework.TestCase;

import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.auth.AuthUtilities;

import java.util.List;
import java.util.ArrayList;

/**
 * Test the AuthUtilities.
 *
 * @author Anthony Eden
 */
public class AuthUtilitiesTest extends TestCase {

    public void testSignature1() {
        String secret = "foobarbaz";
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", "flickr.auth.getFrob"));
        parameters.add(new Parameter("api_key", "987654321"));
        String apiSig = AuthUtilities.getSignature(secret, parameters);
        assertEquals("5f3870be274f6c49b3e31a0c6728957f", apiSig);
    }

    public void testSignature2() {
        String secret = "SECRET";
        List parameters = new ArrayList();
        parameters.add(new Parameter("foo", "1"));
        parameters.add(new Parameter("bar", "2"));
        parameters.add(new Parameter("baz", "3"));
        String apiSig = AuthUtilities.getSignature(secret, parameters);
        assertEquals("1f3870be274f6c49b3e31a0c6728957f", apiSig);
    }

    public void testSignature3() {
        String secret = "foobar";
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", "flickr.auth.getFrob"));
        parameters.add(new Parameter("api_key", "xxx"));
        String apiSig = AuthUtilities.getSignature(secret, parameters);
        assertEquals("DA322D3B88BD67A4551993F7E68D39D4".toLowerCase(), apiSig);
    }

}
