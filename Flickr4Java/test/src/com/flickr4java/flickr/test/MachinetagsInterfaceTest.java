package com.flickr4java.flickr.test;

import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.machinetags.MachinetagsInterface;
import com.flickr4java.flickr.machinetags.Namespace;
import com.flickr4java.flickr.machinetags.NamespacesList;
import com.flickr4java.flickr.machinetags.Pair;
import com.flickr4java.flickr.machinetags.Predicate;
import com.flickr4java.flickr.machinetags.Value;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Calendar;

/**
 * @author mago
 * @version $Id: MachinetagsInterfaceTest.java,v 1.2 2009/06/21 19:55:15 x-mago Exp $
 */
public class MachinetagsInterfaceTest {
    Flickr flickr = null;
    private TestProperties testProperties;

    @Before
    public void setUp() throws
    ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
        testProperties = new TestProperties();

        REST rest = new REST();

        flickr = new Flickr(
                testProperties.getApiKey(),
                testProperties.getSecret(),
                rest
                );

        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    @Test
    public void testGetNamespaces()
            throws FlickrException, IOException, SAXException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String predicate = "collection";
        int page = 1;
        int perPage = 100;
        NamespacesList list = machinetagsInterface.getNamespaces(predicate, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for(int i = 0;i < list.size();i++) {
            Namespace ns = (Namespace) list.get(i);
            if(ns.getValue().equals("content")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetPredicates()
            throws FlickrException, IOException, SAXException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "all";
        int page = 1;
        int perPage = 100;
        NamespacesList list = machinetagsInterface.getPredicates(namespace, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for(int i = 0;i < list.size();i++) {
            Predicate ns = (Predicate) list.get(i);
            if(ns.getValue().equals("groups")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetPairs()
            throws FlickrException, IOException, SAXException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "ceramics";
        String predicate = "material";
        int page = 1;
        int perPage = 100;
        NamespacesList list = machinetagsInterface.getPairs(namespace, null, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (int i = 0; i < list.size(); i++) {
            Pair pair = (Pair) list.get(i);
            if (pair.getValue().equals("ceramics:title")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetValues()
            throws FlickrException, IOException, SAXException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "ceramics";
        String predicate = "material";
        int page = 1;
        int perPage = 100;
        NamespacesList list = machinetagsInterface.getValues(namespace, predicate, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (int i = 0; i < list.size(); i++) {
            Value value = (Value) list.get(i);
            if (value.getValue().equals("porcelain")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetRecentValues()
            throws FlickrException, IOException, SAXException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "ceramics";
        String predicate = "material";
        Calendar addedSince = Calendar.getInstance();
        addedSince.add(Calendar.YEAR, -5);
        NamespacesList list = machinetagsInterface.getRecentValues(namespace, predicate, addedSince.getTime());
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (int i = 0; i < list.size(); i++) {
            Value value = (Value) list.get(i);
            if (value.getValue().equals("porcelain")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }
}
