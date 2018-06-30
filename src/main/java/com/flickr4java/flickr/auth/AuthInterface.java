/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.auth;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.util.ByteUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.apache.log4j.Logger;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.Element;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Authentication interface.
 * 
 * @author Anthony Eden
 */
public class AuthInterface {

    /**
     * The "callback url" passed to Flickr if not specified by caller.
     */
    private static final String OUT_OF_BOUND_AUTH_METHOD = "oob";

    private static final String METHOD_CHECK_TOKEN = "flickr.auth.oauth.checkToken";

    private static final String METHOD_EXCHANGE_TOKEN = "flickr.auth.oauth.getAccessToken";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    private final static Logger logger = Logger.getLogger(AuthInterface.class);

    private int maxGetTokenRetries = 3;

    /**
     * Construct the AuthInterface.
     * 
     * @param apiKey
     *            The API key
     * @param transport
     *            The Transport interface
     */
    public AuthInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Get the OAuth request token - this is step one of authorization.
     * 
     * @return the {@link Token}, store this for when the user returns from the Flickr website.
     */
    public Token getRequestToken() {

        return getRequestToken(null);
    }

    /**
     * Get the OAuth request token - this is step one of authorization.
     * 
     * @param callbackUrl
     *            optional callback URL - required for web auth flow, will be set to "oob" if not specified.
     * @return the {@link Token}, store this for when the user returns from the Flickr website.
     */
    public Token getRequestToken(String callbackUrl) {

        String callback = (callbackUrl != null) ? callbackUrl : OUT_OF_BOUND_AUTH_METHOD;

        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(apiKey).apiSecret(sharedSecret).callback(callback).build();

        return service.getRequestToken();
    }

    /**
     * Get the auth URL, this is step two of authorization.
     * 
     * @param oAuthRequestToken
     *            the token from a {@link AuthInterface#getRequestToken} call.
     */
    public String getAuthorizationUrl(Token oAuthRequestToken, Permission permission) {

        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(apiKey).apiSecret(sharedSecret).build();

        String authorizationUrl = service.getAuthorizationUrl(oAuthRequestToken);
        return String.format("%s&perms=%s", authorizationUrl, permission.toString());
    }

    /**
     * Trade the request token for an access token, this is step three of authorization.
     * 
     * @param oAuthRequestToken
     *            this is the token returned by the {@link AuthInterface#getRequestToken} call.
     * @param verifier
     *            the Verifier created from the code entered by a user or passed back to a callback URL.
     */
    @SuppressWarnings("boxing")
    public Token getAccessToken(Token oAuthRequestToken, Verifier verifier) {
        OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(apiKey).apiSecret(sharedSecret).build();

        // Flickr seems to return invalid token sometimes so retry a few times.
        // See http://www.flickr.com/groups/api/discuss/72157628028927244/
        Token accessToken = null;
        boolean success = false;
        for (int i = 0; i < maxGetTokenRetries && !success; i++) {
            try {
                accessToken = service.getAccessToken(oAuthRequestToken, verifier);
                success = true;
            } catch (OAuthException e) {
                if (i == maxGetTokenRetries - 1) {
                    logger.error(String.format("OAuthService.getAccessToken failing after %d tries, re-throwing exception", i), e);
                    throw e;
                } else {
                    logger.warn(String.format("OAuthService.getAccessToken failed, try number %d: %s", i, e.getMessage()));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        // Do nothing
                    }
                }
            }
        }

        assert accessToken != null;
        assert success;

        return accessToken;
    }

    /**
     * Returns the credentials attached to an OAuth authentication token.
     * 
     * @param accessToken
     *            The authentication token
     * @return The Auth object
     * @throws FlickrException
     */
    public Auth checkToken(Token accessToken) throws FlickrException {

        return checkToken(accessToken.getToken(), accessToken.getSecret());
    }

    /**
     * 
     * @param authToken
     *            The authentication token
     * @param tokenSecret
     * @return The Auth object
     * @throws FlickrException
     * @see "http://www.flickr.com/services/api/flickr.auth.oauth.checkToken.html"
     */
    public Auth checkToken(String authToken, String tokenSecret) throws FlickrException {

        // Use TreeMap so keys are automatically sorted alphabetically
        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("method", METHOD_CHECK_TOKEN);
        parameters.put("oauth_token", authToken);
        parameters.put(Flickr.API_KEY, apiKey);
        // This method call must be signed using Flickr (not OAuth) style signing
        parameters.put("api_sig", getSignature(sharedSecret, parameters));

        Response response = transportAPI.getNonOAuth(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Auth auth = constructAuth(response, authToken, tokenSecret);
        return auth;
    }

    /**
     * Exchange an auth token from the old Authentication API, to an OAuth access token.
     * 
     * Calling this method will delete the auth token used to make the request.
     * 
     * @param authToken
     * @throws FlickrException
     * @see "http://www.flickr.com/services/api/flickr.auth.oauth.getAccessToken.html"
     */
    public Token exchangeAuthToken(String authToken) throws FlickrException {

        // Use TreeMap so keys are automatically sorted alphabetically
        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("method", METHOD_EXCHANGE_TOKEN);
        parameters.put(Flickr.API_KEY, apiKey);
        // This method call must be signed using Flickr (not OAuth) style signing
        parameters.put("api_sig", getSignature(sharedSecret, parameters));

        Response response = transportAPI.getNonOAuth(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Token accessToken = constructToken(response);

        return accessToken;
    }

    /**
     * 
     * @param response
     * @param tokenSecret
     * @param authToken
     */
    private Auth constructAuth(Response response, String authToken, String tokenSecret) {
        Auth auth = new Auth();
        Element authElement = response.getPayload();
        auth.setToken(authToken);
        auth.setTokenSecret(tokenSecret);
        auth.setPermission(Permission.fromString(XMLUtilities.getChildValue(authElement, "perms")));

        Element userElement = XMLUtilities.getChild(authElement, "user");
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(userElement.getAttribute("username"));
        user.setRealName(userElement.getAttribute("fullname"));
        auth.setUser(user);
        return auth;
    }

    /**
     * Construct a Access Token from a Flickr Response.
     * 
     * @param response
     */
    private Token constructToken(Response response) {
        Element authElement = response.getPayload();
        String oauthToken = XMLUtilities.getChildValue(authElement, "oauth_token");
        String oauthTokenSecret = XMLUtilities.getChildValue(authElement, "oauth_token_secret");

        Token token = new Token(oauthToken, oauthTokenSecret);
        return token;
    }

    /**
     * Get a signature for a list of parameters using the given shared secret.
     * 
     * @param sharedSecret
     *            The shared secret
     * @param params
     *            The parameters
     * @return The signature String
     */
    private String getSignature(String sharedSecret, Map<String, String> params) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(sharedSecret);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append(entry.getValue());
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return ByteUtilities.toHexString(md.digest(buffer.toString().getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException u) {
            throw new RuntimeException(u);
        }
    }

    public void setMaxGetTokenRetries(int maxGetTokenRetries) {

        this.maxGetTokenRetries = maxGetTokenRetries;
    }
}
