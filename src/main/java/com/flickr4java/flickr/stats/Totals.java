package com.flickr4java.flickr.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stats information as returned by the stats interface.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class Totals {

    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(Totals.class);

    private int total;

    private int photos;

    private int photostream;

    private int sets;

    private int collections;

    public Totals() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotal(String total) {
        try {
            setTotal(Integer.parseInt(total));
        } catch (NumberFormatException e) {
            // ignore and set value as 0
            setTotal(0);
        }
    }

    public int getPhotos() {
        return photos;
    }

    public void setPhotos(int photos) {
        this.photos = photos;
    }

    public void setPhotos(String photos) {
        try {
            setPhotos(Integer.parseInt(photos));
        } catch (NumberFormatException e) {
            // ignore and set value as 0
            setPhotos(0);
        }
    }

    public int getPhotostream() {
        return photostream;
    }

    public void setPhotostream(int photostream) {
        this.photostream = photostream;
    }

    public void setPhotostream(String photostream) {
        try {
            setPhotostream(Integer.parseInt(photostream));
        } catch (NumberFormatException e) {
            // ignore and set value as null
            setPhotostream(0);
        }
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setSets(String sets) {
        try {
            setSets(Integer.parseInt(sets));
        } catch (NumberFormatException e) {
            // ignore and set value as null
            setSets(0);
        }
    }

    public int getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }

    public void setCollections(String collections) {
        try {
            setCollections(Integer.parseInt(collections));
        } catch (NumberFormatException e) {
            // ignore and set value as null
            setCollections(0);
        }
    }

    @SuppressWarnings("boxing")
    @Override
    public String toString() {

        return String.format("total (%d), photostream (%d), photos (%d), sets (%d), collections (%d)", total, photostream, photos, sets, collections);
    }
}
