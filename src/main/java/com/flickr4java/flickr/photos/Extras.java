/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

import java.util.HashSet;
import java.util.Set;

/**
 * Extra-attributes for Photo-requests.
 * 
 * @author Anthony Eden
 * @version $Id: Extras.java,v 1.12 2009/07/23 20:41:03 x-mago Exp $
 */
public class Extras {

    public static final String KEY_EXTRAS = "extras";

    public static final String LICENSE = "license";

    public static final String DATE_UPLOAD = "date_upload";

    public static final String DATE_TAKEN = "date_taken";

    public static final String OWNER_NAME = "owner_name";

    public static final String ICON_SERVER = "icon_server";

    public static final String ORIGINAL_FORMAT = "original_format";

    public static final String LAST_UPDATE = "last_update";

    public static final String GEO = "geo";

    public static final String TAGS = "tags";

    public static final String MACHINE_TAGS = "machine_tags";

    public static final String O_DIMS = "o_dims";

    public static final String MEDIA = "media";

    public static final String VIEWS = "views";

    public static final String PATH_ALIAS = "path_alias";

    public static final String URL_S = "url_s";

    public static final String URL_SQ = "url_sq";

    public static final String URL_T = "url_t";

    public static final String URL_M = "url_m";

    public static final String URL_L = "url_l";

    public static final String URL_O = "url_o";

    /**
     * Set of all extra-arguments. Used for requesting lists of photos.
     * 
     * @see com.flickr4java.flickr.groups.pools.PoolsInterface#getPhotos(String, String[], Set, int, int)
     * @see com.flickr4java.flickr.panda.PandaInterface#getPhotos(com.flickr4java.flickr.panda.Panda, Set, int, int)
     * @see com.flickr4java.flickr.people.PeopleInterface#getPublicPhotos(String, Set, int, int)
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getContactsPublicPhotos(String, Set, int, boolean, boolean, boolean)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getWithGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set, int,
     *      int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getWithoutGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set,
     *      int, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#recentlyUpdated(java.util.Date, Set, int, int)
     * @see com.flickr4java.flickr.photos.SearchParameters#setExtras(Set)
     * @see com.flickr4java.flickr.photos.geo.GeoInterface#photosForLocation(GeoData, Set, int, int)
     * @see com.flickr4java.flickr.interestingness.InterestingnessInterface#getList(java.util.Date, Set, int, int)
     * @see com.flickr4java.flickr.favorites.FavoritesInterface#getList(String, int, int, Set)
     */
    public static final Set<String> ALL_EXTRAS = new HashSet<String>();

    /**
     * Minimal Set of extra-arguments. Used by convenience-methods that request lists of photos.
     * 
     * @see com.flickr4java.flickr.groups.pools.PoolsInterface#getPhotos(String, String[], Set, int, int)
     * @see com.flickr4java.flickr.panda.PandaInterface#getPhotos(com.flickr4java.flickr.panda.Panda, Set, int, int)
     * @see com.flickr4java.flickr.people.PeopleInterface#getPublicPhotos(String, Set, int, int)
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getContactsPublicPhotos(String, Set, int, boolean, boolean, boolean)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getWithGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set, int,
     *      int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getWithoutGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set,
     *      int, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#recentlyUpdated(java.util.Date, Set, int, int)
     * @see com.flickr4java.flickr.photos.geo.GeoInterface#photosForLocation(GeoData, Set, int, int)
     * @see com.flickr4java.flickr.interestingness.InterestingnessInterface#getList(java.util.Date, Set, int, int)
     * @see com.flickr4java.flickr.favorites.FavoritesInterface#getList(String, int, int, Set)
     */
    public static final Set<String> MIN_EXTRAS = new HashSet<String>();

    static {
        ALL_EXTRAS.add(DATE_TAKEN);
        ALL_EXTRAS.add(DATE_UPLOAD);
        ALL_EXTRAS.add(ICON_SERVER);
        ALL_EXTRAS.add(LAST_UPDATE);
        ALL_EXTRAS.add(LICENSE);
        ALL_EXTRAS.add(ORIGINAL_FORMAT);
        ALL_EXTRAS.add(OWNER_NAME);
        ALL_EXTRAS.add(GEO);
        ALL_EXTRAS.add(TAGS);
        ALL_EXTRAS.add(MACHINE_TAGS);
        ALL_EXTRAS.add(O_DIMS);
        ALL_EXTRAS.add(MEDIA);
        ALL_EXTRAS.add(VIEWS);
        ALL_EXTRAS.add(PATH_ALIAS);
        ALL_EXTRAS.add(URL_S);
        ALL_EXTRAS.add(URL_SQ);
        ALL_EXTRAS.add(URL_T);
        ALL_EXTRAS.add(URL_M);
        ALL_EXTRAS.add(URL_O);
        ALL_EXTRAS.add(URL_L);
    }

    static {
        MIN_EXTRAS.add(ORIGINAL_FORMAT);
        MIN_EXTRAS.add(OWNER_NAME);
    }

    /**
     * No-op constructor.
     */
    private Extras() {
    }

}
