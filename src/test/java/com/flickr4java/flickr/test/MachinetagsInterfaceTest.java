package com.flickr4java.flickr.test;

import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.machinetags.MachinetagsInterface;
import com.flickr4java.flickr.machinetags.Namespace;
import com.flickr4java.flickr.machinetags.NamespacesList;
import com.flickr4java.flickr.machinetags.Pair;
import com.flickr4java.flickr.machinetags.Predicate;
import com.flickr4java.flickr.machinetags.Value;

import org.junit.Test;

import java.util.Calendar;

/**
 * @author mago
 * @version $Id: MachinetagsInterfaceTest.java,v 1.2 2009/06/21 19:55:15 x-mago Exp $
 */
public class MachinetagsInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetNamespaces() throws FlickrException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String predicate = "collection";
        int page = 1;
        int perPage = 100;
        NamespacesList<Namespace> list = machinetagsInterface.getNamespaces(predicate, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (int i = 0; i < list.size(); i++) {
            Namespace ns = list.get(i);
            if (ns.getValue().equals("content")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetPredicates() throws FlickrException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "all";
        int page = 1;
        int perPage = 100;
        NamespacesList<Predicate> list = machinetagsInterface.getPredicates(namespace, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (Predicate ns : list) {
            if (ns.getValue().equals("groups")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetPairs() throws FlickrException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "ceramics";
        int page = 1;
        int perPage = 100;
        NamespacesList<Pair> list = machinetagsInterface.getPairs(namespace, null, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (Pair pair : list) {
            if (pair.getValue().equals("ceramics:title")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetValues() throws FlickrException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "ceramics";
        String predicate = "material";
        int page = 1;
        int perPage = 100;
        NamespacesList<Value> list = machinetagsInterface.getValues(namespace, predicate, perPage, page);
        assertTrue(list.size() > 3);
        boolean contentFound = false;
        for (Value value : list) {
            if (value.getValue().equals("porcelain")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }

    @Test
    public void testGetRecentValues() throws FlickrException {
        MachinetagsInterface machinetagsInterface = flickr.getMachinetagsInterface();
        String namespace = "filmdev";
        String predicate = "recipe";
        Calendar addedSince = Calendar.getInstance();
        addedSince.add(Calendar.YEAR, -10);
        NamespacesList<Value> list = machinetagsInterface.getRecentValues(namespace, predicate, addedSince.getTime());
        assertTrue(list.size() >= 3);
        boolean contentFound = false;
        for (Value value : list) {
            if (value.getValue().equals("8040")) {
                contentFound = true;
            }
        }
        assertTrue(contentFound);
    }
}
