package com.flickr4java.flickr.places;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Lookup Flickr Places.
 * <p>
 * 
 * Announcement on places from yahoo:
 * <p>
 * 
 * <PRE>
 * From: kellan - kellan@yahoo-inc.com
 * Date: Fri, 11 Jan 2008 15:57:59 -0800
 * Subject: [yws-flickr] Flickr and "Place IDs"
 * 
 * At Flickr we've got a really big database that lists a significant
 * percentage of the places that exist in the world, and a few that don't.
 * When you geotag a photo we try to identify the "place" (neighborhood,
 * village, city, county, state, or country) where the photo was taken. And
 * we assign that photo a "place ID".
 * 
 * A place ID is a globally unique identifier for a place on Earth.  A city
 * has a place ID, so do counties, states, and countries.  Even some
 * neighborhoods and landmarks have them, though Flickr isn't currently
 * tracking those. And we're starting to expose these place IDs around Flickr.
 * 
 * ### Place IDs and flickr.photos.search()
 * 
 * The Flickr API method flickr.photos.search() now accepts place_id as an
 * argument.  Along with all of the other parameters you can
 * search on you can now scope your search to a given place.   Historically
 * you've been able to pass bounding boxes to the API, but calculating the
 * right bounding box for a city is tricky, and you can get noise and bad
 * results around the edge.  Now you can pass a single non-ambiguous string
 * and get photos geotagged in San Francisco, CA, or Ohio, or Beijing.
 * (kH8dLOubBZRvX_YZ, LtkqzVqbApjAbJxv, and wpK7URqbAJnWB90W respectively)
 * 
 * The documentation has been updated at:
 * http://www.flickr.com/services/api/flickr.photos.search.html
 * 
 * ### Sources of Place IDs
 * 
 * Place IDs are now returned from a number of source:
 * flickr.photos.getInfo will return place IDs for geotagged photos
 * available as a microformat on the appropriate Places page
 * flickr.places.resolvePlaceURL, and flickr.places.resolvePlaceId are
 * available for round tripping Flickr Places URLs.
 * 
 * http://www.flickr.com/services/api/flickr.photos.getInfo.html
 * http://www.flickr.com/services/api/flickr.places.resolvePlaceURL.html
 * http://www.flickr.com/services/api/flickr.places.resolvePlaceId.html
 * 
 * ### More Place IDs
 * 
 * Right now you can also place IDs in the places URL, and pass them to the
 * map like so:
 * 
 * http://flickr.com/places/wpK7URqbAJnWB90W
 * http://flickr.com/map?place_id=kH8dLOubBZRvX_YZ
 * 
 * ### Place IDs elsewhere
 * 
 * The especially eagle-eyed among you might recognize Place IDs.  Upcoming
 * has been quietly using them for months to uniquely identify their metros.
 * 
 * See events from San Francisco at:
 * http://upcoming.yahoo.com/place/kH8dLOubBZRvX_YZ
 * 
 * See photos from San Francisco at: http://flickr.com/places/kH8dLOubBZRvX_YZ
 * 
 * Additionally Yahoo's skunkworks project FireEagle will also support
 * place IDs.
 * 
 * And yes, there is more work to do, but we're exciting about this as a start.
 * 
 * Thanks,
 * -kellan
 * </PRE>
 * 
 * @author mago
 * @version $Id: PlacesInterface.java,v 1.10 2011/07/02 19:47:35 x-mago Exp $
 */
public class PlacesInterface {
    private static final String METHOD_FIND = "flickr.places.find";

    private static final String METHOD_FIND_BY_LATLON = "flickr.places.findByLatLon";

    private static final String METHOD_RESOLVE_PLACE_ID = "flickr.places.resolvePlaceId";

    private static final String METHOD_RESOLVE_PLACE_URL = "flickr.places.resolvePlaceURL";

    private static final String METHOD_GET_CHILDREN_WITH_PHOTOS_PUBLIC = "flickr.places.getChildrenWithPhotosPublic";

    private static final String METHOD_GET_INFO = "flickr.places.getInfo";

    private static final String METHOD_GET_INFO_BY_URL = "flickr.places.getInfoByUrl";

    private static final String METHOD_GET_PLACETYPES = "flickr.places.getPlaceTypes";

    private static final String METHOD_GET_SHAPEHISTORY = "flickr.places.getShapeHistory";

    private static final String METHOD_GET_TOP_PLACES_LIST = "flickr.places.getTopPlacesList";

    private static final String METHOD_PLACES_FOR_BOUNDINGBOX = "flickr.places.placesForBoundingBox";

    private static final String METHOD_PLACES_FOR_CONTACTS = "flickr.places.placesForContacts";

    private static final String METHOD_PLACES_FOR_TAGS = "flickr.places.placesForTags";

    private static final String METHOD_PLACES_FOR_USER = "flickr.places.placesForUser";

    private static final String METHOD_TAGS_FOR_PLACE = "flickr.places.tagsForPlace";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public PlacesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Return a list of place IDs for a query string.
     * 
     * The flickr.places.find method is not a geocoder. It will round "up" to the nearest place type to which place IDs apply. For example, if you pass it a
     * street level address it will return the city that contains the address rather than the street, or building, itself.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param query
     * @return PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> find(String query) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_FIND);

        parameters.put("query", query);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Return a place ID for a latitude, longitude and accuracy triple.
     * <p>
     * 
     * The flickr.places.findByLatLon method is not meant to be a (reverse) geocoder in the traditional sense. It is designed to allow users to find photos for
     * "places" and will round up to the nearest place type to which corresponding place IDs apply.
     * <p>
     * 
     * For example, if you pass it a street level coordinate it will return the city that contains the point rather than the street, or building, itself.
     * <p>
     * 
     * It will also truncate latitudes and longitudes to three decimal points.
     * <p>
     * 
     * The gory details :
     * 
     * This is (most of) the same magic that is performed when you geotag one of your photos on the site itself. We know that at the neighbourhood level this
     * can get messy and not always return the correct location.
     * <p>
     * 
     * At the city level things are much better but there may still be some gotchas floating around. Sometimes it's as simple as a bug and other times it is an
     * issue of two competing ideas of where a place "is".
     * <p>
     * 
     * This comes with the territory and we are eager to identify and wherever possible fix the problems so when you see something that looks wrong please be
     * gentle :-)
     * <p>
     * 
     * (Reports of incorrect places sent to mailing list will not be ignored but it would be better if you could use the forums for that sort of thing.)
     * <p>
     * 
     * Also, as we do on the site if we can not identify a location for a point as a specific accuracy we pop up the stack and try again. For example, if we
     * can't find a city for a given set of coordinates we try instead to locate the state.
     * <p>
     * 
     * As mentioned above, this method is not designed to serve as a general purpose (reverse) geocoder which is partly reflected by the truncated lat/long
     * coordinates.
     * <p>
     * 
     * If you think that three decimal points are the cause of wonky results locating photos for places, we are happy to investigate but until then it should be
     * All Good (tm).
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param latitude
     *            The latitude whose valid range is -90 to 90. Anything more than 4 decimal places will be truncated.
     * @param longitude
     *            The longitude whose valid range is -180 to 180. Anything more than 4 decimal places will be truncated.
     * @param accuracy
     * @return A PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> findByLatLon(double latitude, double longitude, int accuracy) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_FIND_BY_LATLON);

        parameters.put("lat", "" + Double.toString(latitude));
        parameters.put("lon", "" + Double.toString(longitude));
        parameters.put("accuracy", "" + Integer.toString(accuracy));

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * <p>
     * Return a list of locations with public photos that are parented by a Where on Earth (WOE) or Places ID.
     * </p>
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param placeId
     *            A Flickr Places ID. Can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId
     *            A Where On Earth (WOE) ID. Can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @return List of Places
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> getChildrenWithPhotosPublic(String placeId, String woeId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_GET_CHILDREN_WITH_PHOTOS_PUBLIC);

        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Get informations about a place.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param placeId
     *            A Flickr Places ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @return A Location
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Location getInfo(String placeId, String woeId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);

        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element locationElement = response.getPayload();
        return parseLocation(locationElement);
    }

    /**
     * Lookup information about a place, by its flickr.com/places URL.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param url
     * @return A Location
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Location getInfoByUrl(String url) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO_BY_URL);

        parameters.put("url", url);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element locationElement = response.getPayload();
        return parseLocation(locationElement);
    }

    /**
     * Fetches a list of available place types for Flickr.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @return A list of placetypes
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public ArrayList<PlaceType> getPlaceTypes() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PLACETYPES);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        ArrayList<PlaceType> placeTypeList = new ArrayList<PlaceType>();
        Element placeTypeElement = response.getPayload();
        NodeList placeTypeNodes = placeTypeElement.getElementsByTagName("place_type");
        for (int i = 0; i < placeTypeNodes.getLength(); i++) {
            placeTypeElement = (Element) placeTypeNodes.item(i);
            PlaceType placeType = new PlaceType();
            placeType.setPlaceTypeId(placeTypeElement.getAttribute("id"));
            placeType.setPlaceTypeName(XMLUtilities.getValue(placeTypeElement));
            placeTypeList.add(placeType);
        }
        return placeTypeList;
    }

    /**
     * Return an historical list of all the shape data generated for a Places or Where on Earth (WOE) ID.
     * <p>
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * <p>
     * Not working. As it was not possible to find any results. Not even the ones, that have been described in the announcement of this feature.
     * </p>
     * 
     * @param placeId
     *            A Flickr Places ID. Optional, can be null.
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null.
     * @return A list of shapes
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public ShapeDataList<ShapeData> getShapeHistory(String placeId, String woeId) throws FlickrException {
        ShapeDataList<ShapeData> shapeList = new ShapeDataList<ShapeData>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_SHAPEHISTORY);

        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element shapeElements = response.getPayload();
        shapeList.setTotal(Integer.parseInt(shapeElements.getAttribute("total")));
        shapeList.setWoeId(shapeElements.getAttribute("woe_id"));
        shapeList.setPlaceId(shapeElements.getAttribute("place_id"));
        shapeList.setPlaceType(shapeElements.getAttribute("place_type"));
        shapeList.setPlaceTypeId(Integer.parseInt(shapeElements.getAttribute("place_type_id")));
        NodeList shapeNodes = shapeElements.getElementsByTagName("shape");
        for (int i = 0; i < shapeNodes.getLength(); i++) {
            Element shapeElement = (Element) shapeNodes.item(i);
            ShapeData data = new ShapeData();
            data.setAlpha(Double.parseDouble(shapeElement.getAttribute("alpha")));
            data.setCountEdges(Integer.parseInt(shapeElement.getAttribute("count_edges")));
            data.setCountPoints(Integer.parseInt(shapeElement.getAttribute("count_points")));
            data.setCreated(shapeElement.getAttribute("created"));
            data.setIsDonutHole("1".equals(shapeElement.getAttribute("is_donuthole")));
            data.setHasDonuthole("1".equals(shapeElement.getAttribute("has_donuthole")));

            Element polyElement = XMLUtilities.getChild(shapeElement, "polylines");
            data.setPolyline(XMLUtilities.getChildValue(polyElement, "polyline"));
            Element urlElement = XMLUtilities.getChild(shapeElement, "urls");
            data.setShapefile(XMLUtilities.getChildValue(urlElement, "shapefile"));
            shapeList.add(data);
        }

        return shapeList;
    }

    /**
     * Return the top 100 most geotagged places for a day.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param placeType
     * @param date
     *            Optional, can be null. The default is yesterday.
     * @param placeId
     *            A Flickr Places ID. Optional, can be null.
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null.
     * @return PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> getTopPlacesList(int placeType, Date date, String placeId, String woeId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_GET_TOP_PLACES_LIST);

        parameters.put("place_type", intPlaceTypeToString(placeType));
        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }
        if (date != null) {
            parameters.put("date", ((DateFormat) SearchParameters.DATE_FORMATS.get()).format(date));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Return all the locations of a matching place type for a bounding box.
     * <p>
     * 
     * The maximum allowable size of a bounding box (the distance between the SW and NE corners) is governed by the place type you are requesting. Allowable
     * sizes are as follows:
     * <ul>
     * <li>neighbourhood: 3km (1.8mi)</li>
     * <li>locality: 7km (4.3mi)</li>
     * <li>county: 50km (31mi)</li>
     * <li>region: 200km (124mi)</li>
     * <li>country: 500km (310mi)</li>
     * <li>continent: 1500km (932mi)</li>
     * </ul>
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param bbox
     * @param placeType
     * @return A PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> placesForBoundingBox(int placeType, String bbox) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_PLACES_FOR_BOUNDINGBOX);

        parameters.put("place_type", intPlaceTypeToString(placeType));
        parameters.put("bbox", bbox);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        placesList.setBBox(placesElement.getAttribute("bbox"));
        placesList.setPlaceType(placesElement.getAttribute("place_type"));
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Return a list of the top 100 unique places clustered by a given placetype for a user's contacts.
     * 
     * @param placeType
     *            Use Type-constants at {@link Place}
     * @param placeId
     *            A Flickr Places ID. Optional, can be null.
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null.
     * @param threshold
     *            The minimum number of photos that a place type must have to be included. If the number of photos is lowered then the parent place type for
     *            that place will be used. Optional, can be null.
     * @param contacts
     *            Search your contacts. Either 'all' or 'ff' for just friends and family. (Optional, default is all)
     * @return A PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> placesForContacts(int placeType, String placeId, String woeId, String threshold, String contacts) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_PLACES_FOR_CONTACTS);

        parameters.put("place_type", intPlaceTypeToString(placeType));
        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }
        if (threshold != null) {
            parameters.put("threshold", threshold);
        }
        if (contacts != null) {
            parameters.put("contacts", contacts);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Return a list of the top 100 unique places clustered by a given placetype for set of tags or machine tags.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param placeTypeId
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param placeId
     *            A Flickr Places ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param threshold
     *            The minimum number of photos that a place type must have to be included. If the number of photos is lowered then the parent place type for
     *            that place will be used. Optional, can be null.
     * @param tags
     *            A String-array of Tags. Photos with one or more of the tags listed will be returned. Optional, can be null.
     * @param tagMode
     *            Either 'any' for an OR combination of tags, or 'all' for an AND combination. Defaults to 'any' if not specified. Optional, can be null.
     * @param machineTags
     * @param machineTagMode
     *            Either 'any' for an OR combination of tags, or 'all' for an AND combination. Defaults to 'any' if not specified. Optional, can be null.
     * @param minUploadDate
     *            Optional, can be null.
     * @param maxUploadDate
     *            Optional, can be null.
     * @param minTakenDate
     *            Optional, can be null.
     * @param maxTakenDate
     *            Optional, can be null.
     * @return A PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> placesForTags(int placeTypeId, String woeId, String placeId, String threshold, String[] tags, String tagMode, String machineTags,
            String machineTagMode, Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_PLACES_FOR_TAGS);

        parameters.put("place_type_id", Integer.toString(placeTypeId));
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }
        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (threshold != null) {
            parameters.put("threshold", threshold);
        }
        if (tags != null) {
            parameters.put("tags", StringUtilities.join(tags, ","));
        }
        if (tagMode != null) {
            parameters.put("tag_mode", tagMode);
        }
        if (machineTags != null) {
            parameters.put("machine_tags", machineTags);
        }
        if (machineTagMode != null) {
            parameters.put("machine_tag_mode", machineTagMode);
        }
        if (minUploadDate != null) {
            parameters.put("min_upload_date", Long.toString(minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.put("max_upload_date", Long.toString(maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.put("min_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(minTakenDate));
        }
        if (maxTakenDate != null) {
            parameters.put("max_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(maxTakenDate));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Return a list of the top 100 unique places clustered by a given placetype for a user.
     * 
     * @param placeType
     *            Use Type-constants at {@link Place}
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null.
     * @param placeId
     *            A Flickr Places ID. Optional, can be null.
     * @param threshold
     *            The minimum number of photos that a place type must have to be included. If the number of photos is lowered then the parent place type for
     *            that place will be used. Optional, can be null.
     * @param minUploadDate
     *            Optional, can be null.
     * @param maxUploadDate
     *            Optional, can be null.
     * @param minTakenDate
     *            Optional, can be null.
     * @param maxTakenDate
     *            Optional, can be null.
     * @return A PlacesList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PlacesList<Place> placesForUser(int placeType, String woeId, String placeId, String threshold, Date minUploadDate, Date maxUploadDate,
            Date minTakenDate, Date maxTakenDate) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        PlacesList<Place> placesList = new PlacesList<Place>();
        parameters.put("method", METHOD_PLACES_FOR_USER);

        parameters.put("place_type", intPlaceTypeToString(placeType));
        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }
        if (threshold != null) {
            parameters.put("threshold", threshold);
        }
        if (minUploadDate != null) {
            parameters.put("min_upload_date", Long.toString(minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.put("max_upload_date", Long.toString(maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.put("min_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(minTakenDate));
        }
        if (maxTakenDate != null) {
            parameters.put("max_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(maxTakenDate));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element placesElement = response.getPayload();
        NodeList placesNodes = placesElement.getElementsByTagName("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setPerPage("" + placesNodes.getLength());
        placesList.setTotal("" + placesNodes.getLength());
        for (int i = 0; i < placesNodes.getLength(); i++) {
            Element placeElement = (Element) placesNodes.item(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Find Flickr Places information by Place ID.
     * 
     * @deprecated This method has been deprecated. It won't be removed but you should use {@link #getInfo(String, String)} instead.
     * @param placeId
     * @return A Location
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    @Deprecated
    public Location resolvePlaceId(String placeId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_RESOLVE_PLACE_ID);

        parameters.put("place_id", placeId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element locationElement = response.getPayload();
        return parseLocation(locationElement);
    }

    /**
     * Find Flickr Places information by Place URL.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @deprecated This method has been deprecated. It won't be removed but you should use {@link PlacesInterface#getInfoByUrl(String)} instead.
     * @param flickrPlacesUrl
     * @return A Location
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    @Deprecated
    public Location resolvePlaceURL(String flickrPlacesUrl) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_RESOLVE_PLACE_URL);

        parameters.put("url", flickrPlacesUrl);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element locationElement = response.getPayload();
        return parseLocation(locationElement);
    }

    /**
     * Return a list of the top 100 unique tags for a Flickr Places or Where on Earth (WOE) ID.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param woeId
     *            A Where On Earth (WOE) ID. Optional, can be null.
     * @param placeId
     *            A Flickr Places ID. Optional, can be null.
     * @param minUploadDate
     *            Optional, can be null.
     * @param maxUploadDate
     *            Optional, can be null.
     * @param minTakenDate
     *            Optional, can be null.
     * @param maxTakenDate
     *            Optional, can be null.
     * @return A list of Tags
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public ArrayList<Tag> tagsForPlace(String woeId, String placeId, Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate)
            throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        ArrayList<Tag> tagsList = new ArrayList<Tag>();
        parameters.put("method", METHOD_TAGS_FOR_PLACE);

        if (woeId != null) {
            parameters.put("woe_id", woeId);
        }
        if (placeId != null) {
            parameters.put("place_id", placeId);
        }
        if (minUploadDate != null) {
            parameters.put("min_upload_date", Long.toString(minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.put("max_upload_date", Long.toString(maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.put("min_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(minTakenDate));
        }
        if (maxTakenDate != null) {
            parameters.put("max_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(maxTakenDate));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element tagsElement = response.getPayload();
        NodeList tagsNodes = tagsElement.getElementsByTagName("tag");
        for (int i = 0; i < tagsNodes.getLength(); i++) {
            Element tagElement = (Element) tagsNodes.item(i);
            Tag tag = new Tag();
            tag.setCount(tagElement.getAttribute("count"));
            tag.setValue(XMLUtilities.getValue(tagElement));
            tagsList.add(tag);
        }
        return tagsList;
    }

    private Location parseLocation(Element locationElement) {
        Location location = new Location();
        Element localityElement = (Element) locationElement.getElementsByTagName("locality").item(0);
        Element countyElement = (Element) locationElement.getElementsByTagName("county").item(0);
        Element regionElement = (Element) locationElement.getElementsByTagName("region").item(0);
        Element countryElement = (Element) locationElement.getElementsByTagName("country").item(0);

        location.setPlaceId(locationElement.getAttribute("place_id"));
        // location.setName(locationElement.getAttribute("name"));
        location.setPlaceUrl(locationElement.getAttribute("place_url"));
        location.setWoeId(locationElement.getAttribute("woeid"));
        location.setLatitude(locationElement.getAttribute("latitude"));
        location.setLongitude(locationElement.getAttribute("longitude"));
        location.setTimezone(locationElement.getAttribute("timezone"));
        location.setName(locationElement.getAttribute("name"));
        location.setWoeName(locationElement.getAttribute("woe_name"));
        location.setIsHasShapeData("1".equals(locationElement.getAttribute("has_shapedata")));
        location.setPlaceType(stringPlaceTypeToInt(locationElement.getAttribute("place_type")));
        location.setLocality(parseLocationPlace(localityElement, Place.TYPE_LOCALITY));
        location.setCounty(parseLocationPlace(countyElement, Place.TYPE_COUNTY));
        location.setRegion(parseLocationPlace(regionElement, Place.TYPE_REGION));
        location.setCountry(parseLocationPlace(countryElement, Place.TYPE_COUNTRY));

        return location;
    }

    private Place parseLocationPlace(Element element, int type) {
        Place place = null;
        if (element != null) {
            place = new Place();
            place.setName(XMLUtilities.getValue(element));
            place.setPlaceId(element.getAttribute("place_id"));
            place.setPlaceUrl(element.getAttribute("place_url"));
            place.setWoeId(element.getAttribute("woeid"));
            place.setLatitude(element.getAttribute("latitude"));
            place.setLongitude(element.getAttribute("longitude"));
            place.setPlaceType(type);
        }

        return place;
    }

    private Place parsePlace(Element placeElement) {
        Place place = new Place();
        place.setPlaceId(placeElement.getAttribute("place_id"));
        place.setPlaceUrl(placeElement.getAttribute("place_url"));
        place.setWoeId(placeElement.getAttribute("woeid"));
        place.setLatitude(placeElement.getAttribute("latitude"));
        place.setLongitude(placeElement.getAttribute("longitude"));
        place.setPhotoCount(placeElement.getAttribute("photo_count"));
        // String typeString = placeElement.getAttribute("place_type");
        // Now the place-Id is directly available
        place.setPlaceType(placeElement.getAttribute("place_type_id"));
        // place.setPlaceType(stringPlaceTypeToInt(typeString));
        place.setName(XMLUtilities.getValue(placeElement));
        return place;
    }

    private int stringPlaceTypeToInt(String typeString) {
        int placeType = 0;
        if (typeString.equals("locality")) {
            placeType = Place.TYPE_LOCALITY;
        } else if (typeString.equals("county")) {
            placeType = Place.TYPE_COUNTY;
        } else if (typeString.equals("region")) {
            placeType = Place.TYPE_REGION;
        } else if (typeString.equals("country")) {
            placeType = Place.TYPE_COUNTRY;
        } else if (typeString.equals("continent")) {
            placeType = Place.TYPE_CONTINENT;
        } else if (typeString.equals("neighbourhood")) {
            placeType = Place.TYPE_NEIGHBOURHOOD;
        }
        return placeType;
    }

    public String intPlaceTypeToString(int placeType) throws FlickrException {
        String placeTypeStr = "";
        if (placeType == Place.TYPE_COUNTRY) {
            placeTypeStr = "country";
        } else if (placeType == Place.TYPE_REGION) {
            placeTypeStr = "region";
        } else if (placeType == Place.TYPE_LOCALITY) {
            placeTypeStr = "locality";
        } else if (placeType == Place.TYPE_CONTINENT) {
            placeTypeStr = "continent";
        } else if (placeType == Place.TYPE_NEIGHBOURHOOD) {
            placeTypeStr = "neighbourhood";
        } else {
            throw new FlickrException("33", "Not a valid place type");
        }
        return placeTypeStr;
    }
}
