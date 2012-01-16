/**
@author acaplan
 */
package com.flickr4java.flickr.auth;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author acaplan
 */

public class FlickrExample {
	private static final String PROTECTED_RESOURCE_URL = "http://www.flickr.com:80/services/rest";

	public static void main(String[] args) {
		
		
		OAuthService service = new ServiceBuilder().provider(FlickrApi.class)
				.apiKey("768fe946d252b119746fda82e1599980").apiSecret("1a3c208e172d3edc").build();

		
		//Token requestToken = new Token("72157628888623229-a12f42ab5664ac54 , 24e42a482d9f5de4")
		Token requestToken = new Token("72157628888623229-a12f42ab5664ac54", "24e42a482d9f5de4");
		
		
		Verifier verifier = new Verifier("209-263-338");
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		request.addQuerystringParameter("contacts", "all");
		request.addQuerystringParameter("method", "flickr.photos.search");
		request.addQuerystringParameter("page", "1");
		request.addQuerystringParameter("per_page", "10");
		request.addQuerystringParameter("user_id", "xxx");

		service.signRequest(accessToken, request);
		System.err.println(request.getCompleteUrl());
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	}

	public static void maxxxin(String[] args) {
		OAuthService service = new ServiceBuilder().provider(FlickrApi.class)
				.apiKey("768fe946d252b119746fda82e1599980").apiSecret("1a3c208e172d3edc").build();
		Scanner in = new Scanner(System.in);

		System.out.println("=== Flickr's OAuth Workflow ===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println();

		System.out.println("Now go and authorize Scribe here:");
		System.out.println(service.getAuthorizationUrl(requestToken));
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	}

}