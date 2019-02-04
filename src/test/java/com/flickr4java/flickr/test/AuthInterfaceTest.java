/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;

/**
 * @author Anthony Eden
 */
public class AuthInterfaceTest extends Flickr4JavaTest {

    @Test
    @Ignore
    // Ignored as test is interactive so would fail a build
    public void testAuthFlow() throws IOException, URISyntaxException, ExecutionException, InterruptedException, FlickrException {

        AuthInterface authInterface = flickr.getAuthInterface();

        OAuth1RequestToken requestToken = authInterface.getRequestToken();

        assertNotNull(requestToken);
        assertNotNull(requestToken.getToken());
        assertNotNull(requestToken.getTokenSecret());
        assertTrue(requestToken.getRawResponse().contains("oauth_callback_confirmed=true"));

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.READ);

        assertNotNull(url);
        assertEquals(
                String.format("http://www.flickr.com/services/oauth/authorize?oauth_token=%s&perms=%s", requestToken.getToken(), Permission.READ.toString()),
                url);

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            System.out.println("Paste this URL into your browser");
            System.out.println(url);
        } else {
            URI uri = new URI(url);
            desktop.browse(uri);
        }

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the given authorization code provided by Flickr auth");
        System.out.print(">>");
        String code = in.nextLine();

        assertNotNull(code);

        OAuth1AccessToken accessToken = authInterface.getAccessToken(requestToken, code);

        assertNotNull(accessToken);
        assertNotNull(accessToken.getToken());
        assertNotNull(accessToken.getTokenSecret());

        Auth checkedAuth = authInterface.checkToken(accessToken);
        assertNotNull(checkedAuth);
        assertEquals(accessToken.getToken(), checkedAuth.getToken());
        assertEquals(accessToken.getTokenSecret(), checkedAuth.getTokenSecret());
        assertEquals(Permission.READ, checkedAuth.getPermission());
        assertNotNull(checkedAuth.getUser());
        assertNotNull(checkedAuth.getUser().getUsername());
    }

    @Test
    @Ignore
    // Ignored as test is interactive so would fail a build
    public void testExchangeToken() throws FlickrException {

        AuthInterface authInterface = flickr.getAuthInterface();

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the Flickr auth token to exchange");
        System.out.print(">>");
        String flickrAuthToken = in.nextLine();

        OAuth1RequestToken oAuthToken = authInterface.exchangeAuthToken(flickrAuthToken);

        assertNotNull(oAuthToken);
        assertNotNull(oAuthToken.getToken());
        assertNotNull(oAuthToken.getTokenSecret());
    }
}
