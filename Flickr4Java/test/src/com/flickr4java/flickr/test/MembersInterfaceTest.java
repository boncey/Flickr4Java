/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.members.Member;
import com.flickr4java.flickr.groups.members.MembersInterface;
import com.flickr4java.flickr.groups.members.MembersList;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author mago
 * @version $Id: MembersInterfaceTest.java,v 1.3 2009/07/11 20:30:27 x-mago Exp $
 */
public class MembersInterfaceTest {

    Flickr flickr = null;
    Properties properties = null;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        //Flickr.debugStream = true;

        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

OAuthService service = new ServiceBuilder().provider(FlickrApi.class).apiKey(properties.getProperty("apiKey"))
    				.apiSecret(properties.getProperty("secret")).build();
            REST rest = new REST();
            rest.setHost(properties.getProperty("host"));

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

    @Test
    public void testGetList() throws FlickrException, IOException, SAXException {
        MembersInterface iface = flickr.getMembersInterface();
        // Group: Urban fragments
        String id = "64262537@N00";
        Set memberTypes = new HashSet();
        memberTypes.add(Member.TYPE_MEMBER);
        memberTypes.add(Member.TYPE_ADMIN);
        memberTypes.add(Member.TYPE_MODERATOR);
        MembersList list = iface.getList(id, memberTypes, 50, 1);
        assertNotNull(list);
        assertEquals(50, list.size());
        Member m = (Member) list.get(10);
        assertTrue(m.getId().indexOf("@") > 0);
        assertTrue(m.getUserName().length() > 0);
        assertTrue(m.getIconFarm() > -1);
        assertTrue(m.getIconServer() > -1);
    }
}
