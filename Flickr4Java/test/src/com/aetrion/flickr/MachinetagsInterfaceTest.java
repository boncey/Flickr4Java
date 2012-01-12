package com.flickr4java.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.machinetags.MachinetagsInterface;
import com.flickr4java.flickr.machinetags.Namespace;
import com.flickr4java.flickr.machinetags.NamespacesList;
import com.flickr4java.flickr.machinetags.Pair;
import com.flickr4java.flickr.machinetags.Predicate;
import com.flickr4java.flickr.machinetags.Value;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author mago
 * @version $Id: MachinetagsInterfaceTest.java,v 1.2 2009/06/21 19:55:15 x-mago Exp $
 */
public class MachinetagsInterfaceTest extends TestCase {
    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws
      ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();

            flickr = new Flickr(
                properties.getProperty("apiKey"),
                properties.getProperty("secret"),
                rest
            );

            RequestContext requestContext = RequestContext.getRequestContext();
            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            auth.setPermission(Permission.READ);
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

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

    public void testGetRecentValues()
      throws FlickrException, IOException, SAXException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "ceramics";
        String predicate = "material";
        Calendar addedSince = Calendar.getInstance();
        addedSince.add(Calendar.YEAR, -1);
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
