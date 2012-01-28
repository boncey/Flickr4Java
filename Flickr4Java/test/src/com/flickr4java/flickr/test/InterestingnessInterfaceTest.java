package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.interestingness.InterestingnessInterface;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * 
 * @version $Id: InterestingnessInterfaceTest.java,v 1.5 2008/01/28 23:01:45 x-mago Exp $
 */
public class InterestingnessInterfaceTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    public InterestingnessInterfaceTest(String arg0) {
        super(arg0);
    }

	protected void setUp() throws Exception {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
    				.apiSecret(properties.getProperty("secret")).build();
            REST rest = new REST();

            flickr = new Flickr(
                properties.getProperty("apiKey"),
                properties.getProperty("secret"),
                rest
            );

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
	 * Test method for 'com.flickr4java.flickr.test.interestingness.InterestingnessInterface.getList(String, Set, int, int)'
	 */
	public void testGetListStringSetIntInt() throws FlickrException, IOException, SAXException {
		assertNotNull(flickr);
		InterestingnessInterface ii = flickr.getInterestingnessInterface();
		assertNotNull(ii);
		PhotoList list = ii.getList("2006-09-11", Extras.ALL_EXTRAS, 7, 9);
		assertNotNull(list);
		assertEquals(7, list.size());
		assertEquals(9, list.getPage());
		assertEquals(7, list.getPerPage());
		assertEquals(50, list.getPages());
		assertEquals(500, list.getTotal());
		assertTrue(list.get(0) instanceof Photo);
		Photo photo = (Photo)list.get(1);
		assertNotNull(photo.getId());
		assertNotNull(photo.getLicense());
		assertNotNull(photo.getOwner());
		
		list = ii.getList("2006-09-11", null, 500, 1);
		assertNotNull(list);
		assertEquals(500, list.size());
		
		list = ii.getList((String)null, Extras.ALL_EXTRAS, 100, 1);
		assertNotNull(list);
		assertEquals(100, list.size());
		photo = (Photo)list.get(0);
		for (int i = list.size() - 1; i >= 0; --i) {
			photo = (Photo)list.get(i);
			if (photo.hasGeoData()) {
				//System.out.println(photo.getId() + " " + photo.getGeoData() + " " + photo.getUrl());
			}
		}
		
		// date in the far future
		try {
			list = ii.getList("2030-01-01", null, 100, 3);
			fail("Expected exception not thrown");
		} catch (FlickrException e) {
			// everything ok. we expect that
		}

    }

    /*
     * Test method for 'com.flickr4java.flickr.test.interestingness.InterestingnessInterface.getList(Date, Set, int, int)'
     */
    public void testGetListDateSetIntInt() {

    }

    /*
     * Test method for 'com.flickr4java.flickr.test.interestingness.InterestingnessInterface.getList()'
     */
    public void testGetList() {

    }

}
