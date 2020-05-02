package com.flickr4java.flickr.util;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.httpclient.jdk.JDKHttpClientConfig;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * OAuth utilities.
 * 
 * @author Vincent Privat
 */
public final class OAuthUtilities {

    private static final Logger logger = LoggerFactory.getLogger(OAuthUtilities.class);

    private OAuthUtilities() {

    }

    /**
     * Creates a new OAuth 1.0.a service.
     * 
     * @param apiKey OAuth API key
     * @param sharedSecret OAuth API secret
     * @param connectTimeoutMs connect timeout in milliseconds
     * @param readTimeoutMs read timeout in milliseconds
     *
     * @return OAuth 1.0.a service
     */
    public static OAuth10aService createOAuthService(String apiKey, String sharedSecret, Integer connectTimeoutMs, Integer readTimeoutMs) {
        JDKHttpClientConfig config = JDKHttpClientConfig.defaultConfig();
        if (connectTimeoutMs != null) {
            config.setConnectTimeout(connectTimeoutMs);
        }
        if (readTimeoutMs != null) {
            config.setReadTimeout(readTimeoutMs);
        }
        ServiceBuilder serviceBuilder = new ServiceBuilder(apiKey).apiSecret(sharedSecret).httpClientConfig(config);

        if (Flickr.debugRequest) {
            serviceBuilder = serviceBuilder.debug();
        }

        return serviceBuilder.build(FlickrApi.instance());
    }

    /**
     * Signs the given OAuth request using the given OAuth service. 
     *
     * @param service OAuth 1.0.a service
     * @param request OAuth request
     * @param proxyCredentials optional proxy credentials, can be null
     */
    public static void signRequest(OAuth10aService service, OAuthRequest request, String proxyCredentials) {
        Auth auth = RequestContext.getRequestContext().getAuth();
        if (auth != null) {
            service.signRequest(new OAuth1AccessToken(auth.getToken(), auth.getTokenSecret()), request);
        }

        if (proxyCredentials != null) {
            request.addHeader("Proxy-Authorization", "Basic " + proxyCredentials);
        }

        if (Flickr.debugRequest) {
            logger.debug("POST: {}", request.getCompleteUrl());
        }
    }

    /**
     * Builds a normal POST request.
     *
     * @param parameters body parameters
     * @param url URL
     * @return OAuth request
     */
    public static OAuthRequest buildNormalPostRequest(Map<String, ?> parameters, String url) {
        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        parameters.entrySet().forEach(e -> request.addBodyParameter(e.getKey(), String.valueOf(e.getValue())));
        return request;
    }

    /**
     * Builds a multipart POST request.
     *
     * @param parameters QueryString parameters
     * @param url URL
     * @return OAuth request
     */
    public static OAuthRequest buildMultipartRequest(Map<String, String> parameters, String url) {
        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        String multipartBoundary = getMultipartBoundary();
        request.initMultipartPayload(multipartBoundary);
        request.addHeader("Content-Type", "multipart/form-data; boundary=" + multipartBoundary);
        parameters.entrySet().forEach(e -> request.addQuerystringParameter(e.getKey(), e.getValue()));
        return request;
    }

    private static String getMultipartBoundary() {
        return "---------------------------" + UUID.randomUUID();
    }
}
