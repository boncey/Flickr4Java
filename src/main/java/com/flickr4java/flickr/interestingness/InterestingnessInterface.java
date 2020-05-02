/*
 *-------------------------------------------------------
 * (c) 2006 Das B&uuml;ro am Draht GmbH - All Rights reserved
 *-------------------------------------------------------
 */
package com.flickr4java.flickr.interestingness;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author till
 * @version $Id: InterestingnessInterface.java,v 1.9 2009/07/11 20:30:27 x-mago Exp $
 */
public class InterestingnessInterface {

    public static final String METHOD_GET_LIST = "flickr.interestingness.getList";

    private static final String KEY_METHOD = "method";

    private static final String KEY_DATE = "date";

    private static final String KEY_EXTRAS = "extras";

    private static final String KEY_PER_PAGE = "per_page";

    private static final String KEY_PAGE = "page";

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public InterestingnessInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Returns the list of interesting photos for the most recent day or a user-specified date.
     * 
     * This method does not require authentication.
     * 
     * @param date
     * @param extras
     *            A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload,
     *            date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage
     *            The number of photos to show per page
     * @param page
     *            The page offset
     * @return PhotoList
     * @throws FlickrException if there was a problem connecting to Flickr
     * @see com.flickr4java.flickr.photos.Extras
     */
    public PhotoList<Photo> getList(String date, Set<String> extras, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PhotoList<Photo> photos = new PhotoList<Photo>();

        parameters.put(KEY_METHOD, METHOD_GET_LIST);

        if (date != null) {
            parameters.put(KEY_DATE, date);
        }

        if (extras != null) {
            parameters.put(KEY_EXTRAS, StringUtilities.join(extras, ","));
        }

        if (perPage > 0) {
            parameters.put(KEY_PER_PAGE, String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put(KEY_PAGE, String.valueOf(page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            Photo photo = PhotoUtils.createPhoto(photoElement);
            photos.add(photo);
        }
        return photos;
    }

    /**
     * 
     * @param date
     * @param extras
     * @param perPage
     * @param page
     * @return PhotoList
     * @throws FlickrException if there was a problem connecting to Flickr
     * @see com.flickr4java.flickr.photos.Extras
     */
    public PhotoList<Photo> getList(Date date, Set<String> extras, int perPage, int page) throws FlickrException {
        String dateString = null;
        if (date != null) {
            DateFormat df = DATE_FORMATS.get();
            dateString = df.format(date);
        }
        return getList(dateString, extras, perPage, page);
    }

    /**
     * convenience method to get the list of all 500 most recent photos in flickr explore with all known extra attributes.
     * 
     * @return a List of Photos
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PhotoList<Photo> getList() throws FlickrException {
        return getList((String) null, Extras.ALL_EXTRAS, 500, 1);
    }

}
