package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.flickr4java.flickr.util.OAuthUtilities;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Parameter;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

public class OAuthUtilitiesTest extends Flickr4JavaTest {

    @Test
    public void testCreateOAuthService() {
        OAuth10aService service = OAuthUtilities.createOAuthService("foo", "bar", null, null);
        assertEquals("foo", service.getApiKey());
        assertEquals("bar", service.getApiSecret());

        service = OAuthUtilities.createOAuthService("foo", "bar", 1, 2);
        assertEquals("foo", service.getApiKey());
        assertEquals("bar", service.getApiSecret());
    }

    @Test
    public void testSignRequest() {
        // No proxy credentials
        OAuth10aService service = new ServiceBuilder("foo").apiSecret("bar").build(FlickrApi.instance());
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://foobar");
        assertTrue(request.getOauthParameters().isEmpty());
        OAuthUtilities.signRequest(service, request, null);
        assertNull(request.getHeaders().get("Proxy-Authorization"));

        // proxy credentials
        service = new ServiceBuilder("foo").apiSecret("bar").build(FlickrApi.instance());
        request = new OAuthRequest(Verb.POST, "http://foobar");
        assertTrue(request.getOauthParameters().isEmpty());
        OAuthUtilities.signRequest(service, request, "creds");
        assertEquals("Basic creds", request.getHeaders().get("Proxy-Authorization"));
    }

    @Test
    public void testBuildNormalPostRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("foo", "bar");
        OAuthRequest request = OAuthUtilities.buildNormalPostRequest(params, "http://foo");
        List<Parameter> bodyParams = request.getBodyParams().getParams();
        assertEquals(1, bodyParams.size());
        assertEquals(new Parameter("foo", "bar"), bodyParams.get(0));
    }

    @Test
    public void testBuildMultipartRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("foo", "bar");
        OAuthRequest request = OAuthUtilities.buildMultipartRequest(params, "http://foo");
        List<Parameter> queryStringParams = request.getQueryStringParams().getParams();
        assertEquals(1, queryStringParams.size());
        assertEquals(new Parameter("foo", "bar"), queryStringParams.get(0));
        String contentType = request.getHeaders().get("Content-Type");
        assertTrue(contentType, contentType.matches(
                "multipart/form-data; boundary=---------------------------[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}"));
    }
}
