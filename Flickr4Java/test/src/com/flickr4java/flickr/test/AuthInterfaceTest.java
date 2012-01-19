/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class AuthInterfaceTest { //extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException {
        Flickr.debugRequest = true;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
    				.apiSecret(properties.getProperty("secret")).build();
            REST rest = new REST(service);
            rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), rest);

			Auth auth = new Auth();
			auth.setPermission(Permission.READ);
			auth.setToken(properties.getProperty("token"));
			auth.setTokenSecret(properties.getProperty("tokensecret"));

			RequestContext requestContext = RequestContext.getRequestContext();
			requestContext.setAuth(auth);
			flickr.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }
/*
    public void testGetFrob() throws FlickrException, IOException, SAXException {
        AuthInterface authInterface = flickr.getAuthInterface();
        String frob = authInterface.getFrob();
        assertNotNull(frob);
    }

    public void testReadAuthentication() throws FlickrException, IOException, SAXException,
            BrowserLaunchingInitializingException, BrowserLaunchingExecutionException, UnsupportedOperatingSystemException {
        testAuthentication(Permission.READ);
    }

    public void testWriteAuthentication() throws FlickrException, IOException, BrowserLaunchingInitializingException,
            SAXException, BrowserLaunchingExecutionException, UnsupportedOperatingSystemException {
        testAuthentication(Permission.WRITE);
    }

    public void testDeleteAuthentication() throws FlickrException, IOException, BrowserLaunchingInitializingException,
            SAXException, BrowserLaunchingExecutionException, UnsupportedOperatingSystemException {
        testAuthentication(Permission.DELETE);
    }

    private void testAuthentication(Permission permission) throws FlickrException, IOException, SAXException,
            BrowserLaunchingInitializingException, BrowserLaunchingExecutionException, UnsupportedOperatingSystemException {
        AuthInterface authInterface = flickr.getAuthInterface();
        String frob = authInterface.getFrob();
        URL url = authInterface.buildAuthenticationUrl(permission, frob);

        BrowserLauncher launcher = new BrowserLauncher(null);
        launcher.openURLinBrowser(url.toString());
        
        // display a dialog
        final JDialog d = new JDialog();
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                d.dispose();
            }
        });
        d.getContentPane().add(continueButton);
        d.setModal(true);
        d.pack();
        d.setVisible(true);

        Auth auth = authInterface.getToken(frob);
//        System.out.println("Token: " + authentication.getToken());
//        System.out.println("Permission: " + authentication.getPermission());
        assertNotNull(auth.getToken());
        assertEquals(permission, auth.getPermission());

        Auth checkedAuth = authInterface.checkToken(auth.getToken());
        assertEquals(auth.getToken(), checkedAuth.getToken());
        assertEquals(auth.getPermission(), checkedAuth.getPermission());
    }

    public void testCheckToken() throws FlickrException, IOException, SAXException {
        String token = properties.getProperty("token");
        AuthInterface authInterface = flickr.getAuthInterface();
        Auth checkedAuth = authInterface.checkToken(token);
        
        assertEquals(token, checkedAuth.getToken());
    }
*/
}
