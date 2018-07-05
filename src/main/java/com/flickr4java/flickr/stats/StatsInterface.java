package com.flickr4java.flickr.stats;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.FlickrRuntimeException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface to the Stats API.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class StatsInterface {

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(StatsInterface.class);

    private static final String METHOD_GET_COLLECTION_DOMAINS = "flickr.stats.getCollectionDomains";

    private static final String METHOD_GET_COLLECTION_REFERRERS = "flickr.stats.getCollectionReferrers";

    private static final String METHOD_GET_COLLECTION_STATS = "flickr.stats.getCollectionStats";

    private static final String METHOD_GET_CSV_FILES = "flickr.stats.getCSVFiles";

    private static final String METHOD_GET_PHOTO_DOMAINS = "flickr.stats.getPhotoDomains";

    private static final String METHOD_GET_PHOTO_REFERRERS = "flickr.stats.getPhotoReferrers";

    private static final String METHOD_GET_PHOTO_STATS = "flickr.stats.getPhotoStats";

    private static final String METHOD_GET_PHOTOSET_DOMAINS = "flickr.stats.getPhotosetDomains";

    private static final String METHOD_GET_PHOTOSET_REFERRERS = "flickr.stats.getPhotosetReferrers";

    private static final String METHOD_GET_PHOTOSET_STATS = "flickr.stats.getPhotosetStats";

    private static final String METHOD_GET_PHOTOSTREAM_DOMAINS = "flickr.stats.getPhotostreamDomains";

    private static final String METHOD_GET_PHOTOSTREAM_REFERRERS = "flickr.stats.getPhotostreamReferrers";

    private static final String METHOD_GET_PHOTOSTREAM_STATS = "flickr.stats.getPhotostreamStats";

    private static final String METHOD_GET_POPULAR_PHOTOS = "flickr.stats.getPopularPhotos";

    private static final String METHOD_GET_TOTAL_VIEWS = "flickr.stats.getTotalViews";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public StatsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get a list of referring domains for a collection.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param collectionId
     *            (Optional) The id of the collection to get stats for. If not provided, stats for all collections will be returned.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getCollectionDomains.html"
     */
    public DomainList getCollectionDomains(Date date, String collectionId, int perPage, int page) throws FlickrException {
        return getDomains(METHOD_GET_COLLECTION_DOMAINS, "collection_id", collectionId, date, perPage, page);
    }

    /**
     * Get a list of referrers from a given domain to a collection.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param domain
     *            (Required) The domain to return referrers for. This should be a hostname (eg: "flickr.com") with no protocol or pathname.
     * @param collectionId
     *            (Optional) The id of the collection to get stats for. If not provided, stats for all collections will be returned.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getCollectionReferrers.html"
     */
    public ReferrerList getCollectionReferrers(Date date, String domain, String collectionId, int perPage, int page) throws FlickrException {
        return getReferrers(METHOD_GET_COLLECTION_REFERRERS, domain, "collection_id", collectionId, date, perPage, page);
    }

    /**
     * Get the number of views, comments and favorites on a collection for a given date.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param collectionId
     *            (Required) The id (from the URL!) of the collection to get stats for.
     * @see "http://www.flickr.com/services/api/flickr.stats.getCollectionStats.htm"
     */
    public Stats getCollectionStats(String collectionId, Date date) throws FlickrException {
        return getStats(METHOD_GET_COLLECTION_STATS, "collection_id", collectionId, date);
    }

    /**
     * Returns a list of URLs for text files containing all your stats data (from November 26th 2007 onwards) for the currently auth'd user.
     * 
     * @throws FlickrException
     * 
     * @see "http://www.flickr.com/services/api/flickr.stats.getCSVFiles.html"
     */
    public List<Csv> getCSVFiles() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CSV_FILES);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        List<Csv> csvFiles = parseCsvFiles(response);

        return csvFiles;
    }

    /**
     * Get a list of referring domains for a photo.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param photoId
     *            (Optional) The id of the photo to get stats for. If not provided, stats for all photos will be returned.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotoDomains.html"
     */
    public DomainList getPhotoDomains(Date date, String photoId, int perPage, int page) throws FlickrException {
        return getDomains(METHOD_GET_PHOTO_DOMAINS, "photo_id", photoId, date, perPage, page);
    }

    /**
     * Get a list of referrers from a given domain to a photo.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param domain
     *            (Required) The domain to return referrers for. This should be a hostname (eg: "flickr.com") with no protocol or pathname.
     * @param photoId
     *            (Optional) The id of the photo to get stats for. If not provided, stats for all photos will be returned.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotoReferrers.html"
     */
    public ReferrerList getPhotoReferrers(Date date, String domain, String photoId, int perPage, int page) throws FlickrException {
        return getReferrers(METHOD_GET_PHOTO_REFERRERS, domain, "photo_id", photoId, date, perPage, page);
    }

    /**
     * Get the number of views, comments and favorites on a photo for a given date.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param photoId
     *            (Required) The id of the photo to get stats for.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotoStats.htm"
     */
    public Stats getPhotoStats(String photoId, Date date) throws FlickrException {
        return getStats(METHOD_GET_PHOTO_STATS, "photo_id", photoId, date);
    }

    /**
     * Get a list of referring domains for a photoset.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param photosetId
     *            (Optional) The id of the photoset to get stats for. If not provided, stats for all photos will be returned.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotosetDomains.html"
     */
    public DomainList getPhotosetDomains(Date date, String photosetId, int perPage, int page) throws FlickrException {
        return getDomains(METHOD_GET_PHOTOSET_DOMAINS, "photoset_id", photosetId, date, perPage, page);
    }

    /**
     * Get a list of referrers from a given domain to a photoset.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param domain
     *            (Required) The domain to return referrers for. This should be a hostname (eg: "flickr.com") with no protocol or pathname.
     * @param photosetId
     *            (Optional) The id of the photoset to get stats for. If not provided, stats for all sets will be returned.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotosetReferrers.html"
     */
    public ReferrerList getPhotosetReferrers(Date date, String domain, String photosetId, int perPage, int page) throws FlickrException {
        return getReferrers(METHOD_GET_PHOTOSET_REFERRERS, domain, "photoset_id", photosetId, date, perPage, page);
    }

    /**
     * Get the number of views, comments and favorites on a photoset for a given date.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param photosetId
     *            (Required) The id of the photoset to get stats for.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotosetStats.htm"
     */
    public Stats getPhotosetStats(String photosetId, Date date) throws FlickrException {
        return getStats(METHOD_GET_PHOTOSET_STATS, "photoset_id", photosetId, date);
    }

    /**
     * Get a list of referring domains for a photostream.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotostreamDomains.html"
     */
    public DomainList getPhotostreamDomains(Date date, int perPage, int page) throws FlickrException {
        return getDomains(METHOD_GET_PHOTOSTREAM_DOMAINS, null, null, date, perPage, page);

    }

    /**
     * Get a list of referrers from a given domain to a user's photostream.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @param domain
     *            (Required) The domain to return referrers for. This should be a hostname (eg: "flickr.com") with no protocol or pathname.
     * @param perPage
     *            (Optional) Number of domains to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotostreamReferrers.html"
     */
    public ReferrerList getPhotostreamReferrers(Date date, String domain, int perPage, int page) throws FlickrException {
        return getReferrers(METHOD_GET_PHOTOSTREAM_REFERRERS, domain, null, null, date, perPage, page);
    }

    /**
     * Get the number of views, comments and favorites on a photostream for a given date.
     * 
     * @param date
     *            (Required) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day.
     * @see "http://www.flickr.com/services/api/flickr.stats.getPhotostreamStats.htm"
     */
    public Stats getPhotostreamStats(Date date) throws FlickrException {
        return getStats(METHOD_GET_PHOTOSTREAM_STATS, null, null, date);
    }

    /**
     * List the photos with the most views, comments or favorites.
     * 
     * @param date
     *            (Optional) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day. If no date is provided, all time view counts will be returned.
     * @param sort
     *            (Optional) The order in which to sort returned photos. Defaults to views. The possible values are views, comments and favorites. Other sort
     *            options are available through flickr.photos.search.
     * @param perPage
     *            (Optional) Number of referrers to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page
     *            (Optional) The page of results to return. If this argument is omitted, it defaults to 1.
     * @throws FlickrException
     * @see "http://www.flickr.com/services/api/flickr.stats.getPopularPhotos.html"
     */
    public PhotoList<Photo> getPopularPhotos(Date date, StatsSort sort, int perPage, int page) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_POPULAR_PHOTOS);
        if (date != null) {
            parameters.put("date", String.valueOf(date.getTime() / 1000L));
        }
        if (sort != null) {
            parameters.put("sort", sort.name());
        }
        addPaginationParameters(parameters, perPage, page);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        return parsePopularPhotos(response);
    }

    /**
     * Get the overall view counts for an account
     * 
     * @param date
     *            (Optional) Stats will be returned for this date. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will
     *            automatically be rounded down to the start of the day. If no date is provided, all time view counts will be returned.
     * @throws FlickrException
     * 
     * @see "http://www.flickr.com/services/api/flickr.stats.getTotalViews.html"
     */
    public Totals getTotalViews(Date date) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_TOTAL_VIEWS);
        if (date != null) {
            parameters.put("date", String.valueOf(date.getTime() / 1000L));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Totals totals = parseTotals(response);

        return totals;
    }

    private DomainList getDomains(String method, String idKey, String idValue, Date date, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", method);
        parameters.put("date", String.valueOf(date.getTime() / 1000L));
        if (idValue != null) {
            parameters.put(idKey, idValue);
        }
        addPaginationParameters(parameters, perPage, page);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        DomainList domains = parseDomains(response);

        return domains;
    }

    private DomainList parseDomains(Response response) {

        Element element = response.getPayload();
        DomainList domains = new DomainList();
        domains.setPage(element.getAttribute("page"));
        domains.setPages(element.getAttribute("pages"));
        domains.setPerPage(element.getAttribute("perpage"));
        domains.setTotal(element.getAttribute("total"));

        NodeList domainElements = element.getElementsByTagName("domain");
        for (int i = 0; i < domainElements.getLength(); i++) {
            Element domainElement = (Element) domainElements.item(i);
            Domain domain = new Domain();
            domain.setName(domainElement.getAttribute("name"));
            domain.setViews(domainElement.getAttribute("views"));
            domains.add(domain);
        }

        return domains;
    }

    private ReferrerList getReferrers(String method, String domain, String idKey, String idValue, Date date, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", method);
        parameters.put("domain", domain);
        parameters.put("date", String.valueOf(date.getTime() / 1000L));
        if (idValue != null) {
            parameters.put(idKey, idValue);
        }
        addPaginationParameters(parameters, perPage, page);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        ReferrerList referrers = parseReferrers(response);

        return referrers;
    }

    private ReferrerList parseReferrers(Response response) {

        Element element = response.getPayload();
        ReferrerList referrers = new ReferrerList();
        referrers.setPage(element.getAttribute("page"));
        referrers.setPages(element.getAttribute("pages"));
        referrers.setPerPage(element.getAttribute("perpage"));
        referrers.setTotal(element.getAttribute("total"));
        referrers.setName(element.getAttribute("name"));

        NodeList referrerElements = element.getElementsByTagName("referrer");
        for (int i = 0; i < referrerElements.getLength(); i++) {
            Element referrerElement = (Element) referrerElements.item(i);
            Referrer referrer = new Referrer();
            referrer.setUrl(referrerElement.getAttribute("url"));
            referrer.setViews(referrerElement.getAttribute("views"));
            referrers.add(referrer);
        }

        return referrers;
    }

    private Stats getStats(String method, String idKey, String idValue, Date date) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", method);
        parameters.put("date", String.valueOf(date.getTime() / 1000L));
        if (idValue != null) {
            parameters.put(idKey, idValue);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Stats stats = parseStats(response.getPayload());

        return stats;
    }

    /**
     * 
     * @param element
     */
    private Stats parseStats(Element element) {
        Stats stats = new Stats();
        String views = element.getAttribute("views");
        String comments = element.getAttribute("comments");
        String favorites = element.getAttribute("favorites");

        stats.setViews(views);
        stats.setComments(comments);
        stats.setFavorites(favorites);

        return stats;
    }

    private Totals parseTotals(Response response) {
        Element element = response.getPayload();

        Totals stats = new Totals();

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {

            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String total = getViewsValue(childElement, "total");
                if (total != null) {
                    stats.setTotal(total);
                }

                String photos = getViewsValue(childElement, "photos");
                if (photos != null) {
                    stats.setPhotos(photos);
                }

                String photostream = getViewsValue(childElement, "photostream");
                if (photostream != null) {
                    stats.setPhotostream(photostream);
                }

                String collections = getViewsValue(childElement, "collections");
                if (collections != null) {
                    stats.setCollections(collections);
                }

                String sets = getViewsValue(childElement, "sets");
                if (sets != null) {
                    stats.setSets(sets);
                }
            }
        }

        return stats;
    }

    private String getViewsValue(Element childElement, String name) {
        String total = null;
        if (childElement.getNodeName().equals(name)) {
            total = childElement.getAttribute("views");
        }

        return total;
    }

    private void addPaginationParameters(Map<String, Object> parameters, int perPage, int page) {
        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }
    }

    private List<Csv> parseCsvFiles(Response response) {
        Element element = response.getPayload();

        List<Csv> csvFiles = new ArrayList<Csv>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        NodeList csvElements = element.getElementsByTagName("csv");
        for (int i = 0; i < csvElements.getLength(); i++) {
            Element csvElement = (Element) csvElements.item(i);
            Csv csv = new Csv();
            csv.setHref(csvElement.getAttribute("href"));
            csv.setType(csvElement.getAttribute("type"));
            try {
                csv.setDate(dateFormat.parse(csvElement.getAttribute("date")));
            } catch (ParseException e) {
                throw new FlickrRuntimeException(e);
            }

            csvFiles.add(csv);
        }
        return csvFiles;
    }

    /**
     * 
     * @param response
     */
    private PhotoList<Photo> parsePopularPhotos(Response response) {

        Element payload = response.getPayload();
        PhotoList<Photo> photos = new PhotoList<Photo>();

        NodeList photoElements = payload.getElementsByTagName("photo");
        for (int i = 0; i < photoElements.getLength(); i++) {
            Element photoElement = (Element) photoElements.item(i);
            Photo photo = PhotoUtils.createPhoto(photoElement);

            Element statsElement = XMLUtilities.getChild(photoElement, "stats");
            Stats stats = parseStats(statsElement);
            photo.setStats(stats);

            photos.add(photo);
        }

        return photos;
    }

}
