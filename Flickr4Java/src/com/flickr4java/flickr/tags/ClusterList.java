package com.flickr4java.flickr.tags;

import java.util.ArrayList;

/**
 * List (tag-)clusters.
 * 
 * @author mago
 * @since 1.2
 * @version $Id: ClusterList.java,v 1.1 2008/07/19 14:42:54 x-mago Exp $
 */
public class ClusterList {

    private ArrayList<Cluster> clusters = new ArrayList<Cluster>();

    public void addCluster(Cluster cluster) {
        clusters.add(cluster);
    }

    public ArrayList<Cluster> getClusters() {
        return clusters;
    }
}
