package com.flickr4java.flickr.test.util;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class Setup {

    public static void main(String[] args) {
        System.out.println("Flickr4Java: Set up integration test environment");
        try {
            new Setup();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        }
    }

    public Setup() throws IOException, FlickrException {
        String propertiesFile = "src/test/resources/setup.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));

        Flickr flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
        Flickr.debugStream = false;
        AuthInterface authInterface = flickr.getAuthInterface();

        Scanner scanner = new Scanner(System.in);

        Token requestToken = authInterface.getRequestToken();

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.DELETE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">> ");

        String tokenKey = scanner.nextLine().trim();

        Token accessToken = authInterface.getAccessToken(requestToken, new Verifier(tokenKey));
        System.out.println("Authentication success");

        Auth auth = authInterface.checkToken(accessToken);

        properties.setProperty("token", accessToken.getToken());
        properties.setProperty("tokensecret", accessToken.getSecret());
        properties.store(new FileOutputStream(propertiesFile), "");

        // This token can be used until the user revokes it.
        System.out.println("Access token - token  = " + accessToken.getToken());
        System.out.println("             - secret = " + accessToken.getSecret());
        System.out.println("(These have been saved to the properties file.)");
        System.out.println("Realname: " + auth.getUser().getRealName());
        System.out.println("Username: " + auth.getUser().getUsername());
        System.out.println("Permission: " + auth.getPermission().getType());
    }
}
