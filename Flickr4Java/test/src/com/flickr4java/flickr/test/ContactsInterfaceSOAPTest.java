/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.contacts.ContactsInterface;
import com.flickr4java.flickr.util.IOUtilities;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.SignatureType;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Matt Ray
 */
public class ContactsInterfaceSOAPTest extends TestCase {

    Flickr flickr = null;
    Properties properties = null;

    @Override
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
                    .apiSecret(properties.getProperty("secret")).signatureType(SignatureType.QueryString).build();
            SOAP soap = new SOAP(service);
            flickr = new Flickr(properties.getProperty("apiKey"),
                    properties.getProperty("secret"), soap);

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

    public void testGetList() throws FlickrException, IOException, SAXException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection contacts = iface.getList();
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
    }

    public void testGetPublicList() throws FlickrException, IOException, SAXException {
        ContactsInterface iface = flickr.getContactsInterface();
        Collection contacts = iface.getPublicList(properties.getProperty("nsid"));
        assertNotNull(contacts);
        assertEquals(1, contacts.size());
    }

}
