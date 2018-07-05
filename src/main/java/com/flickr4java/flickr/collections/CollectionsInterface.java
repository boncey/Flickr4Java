package com.flickr4java.flickr.collections;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for flickr.collections.* methods.
 * 
 * @author Darren Greaves
 * @version $Id$ Copyright (c) 2012 Darren Greaves.
 */
public class CollectionsInterface {

    private static final String METHOD_GET_INFO = "flickr.collections.getInfo";

    private static final String METHOD_GET_TREE = "flickr.collections.getTree";

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(CollectionsInterface.class);

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public CollectionsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Retrieves info on the given Flickr {@link Collection} (of {@link Photoset}s).
     * 
     * This method requires authentication.
     * 
     * @param collectionId
     *            the id of the collection (from the getTree call, not from the collection URL).
     * 
     * @return the given Collection
     * @throws FlickrException
     */
    public Collection getInfo(String collectionId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);
        parameters.put("collection_id", collectionId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Collection collection = parseCollection(response.getPayload());

        return collection;
    }

    /**
     * Retrieves a list of the current Commons institutions.
     * 
     * This method does not require authentication.
     * 
     * @param collectionId
     *            the id of the collection (optional - returns all if not specified).
     * @param userId
     *            the user id of the collection owner (optional - defaults to calling user).
     * 
     * @return List of Institution
     * @throws FlickrException
     */
    public List<Collection> getTree(String collectionId, String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_TREE);
        if (collectionId != null) {
            parameters.put("collection_id", collectionId);
        }
        if (userId != null) {
            parameters.put("user_id", userId);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        List<Collection> collections = new ArrayList<Collection>();
        Element mElement = response.getPayload();

        NodeList collectionElements = mElement.getElementsByTagName("collection");
        for (int i = 0; i < collectionElements.getLength(); i++) {
            Element element = (Element) collectionElements.item(i);
            collections.add(parseTreeCollection(element));
        }
        return collections;
    }

    /**
     * Parse the XML for a collection as returned by getInfo call.
     * 
     * @param collectionElement
     * @return
     */
    private Collection parseCollection(Element collectionElement) {

        Collection collection = new Collection();
        collection.setId(collectionElement.getAttribute("id"));
        collection.setServer(collectionElement.getAttribute("server"));
        collection.setSecret(collectionElement.getAttribute("secret"));
        collection.setChildCount(collectionElement.getAttribute("child_count"));
        collection.setIconLarge(collectionElement.getAttribute("iconlarge"));
        collection.setIconSmall(collectionElement.getAttribute("iconsmall"));
        collection.setDateCreated(collectionElement.getAttribute("datecreate"));
        collection.setTitle(XMLUtilities.getChildValue(collectionElement, "title"));
        collection.setDescription(XMLUtilities.getChildValue(collectionElement, "description"));

        Element iconPhotos = XMLUtilities.getChild(collectionElement, "iconphotos");
        if (iconPhotos != null) {
            NodeList photoElements = iconPhotos.getElementsByTagName("photo");
            for (int i = 0; i < photoElements.getLength(); i++) {
                Element photoElement = (Element) photoElements.item(i);
                collection.addPhoto(PhotoUtils.createPhoto(photoElement));
            }
        }

        return collection;
    }

    /**
     * Parse the XML for a collection as returned by getTree call.
     * 
     * @param collectionElement
     * @return
     */
    private Collection parseTreeCollection(Element collectionElement) {

        Collection collection = new Collection();
        parseCommonFields(collectionElement, collection);
        collection.setTitle(collectionElement.getAttribute("title"));
        collection.setDescription(collectionElement.getAttribute("description"));

        // Collections can contain either sets or collections (but not both)
        NodeList childCollectionElements = collectionElement.getElementsByTagName("collection");
        for (int i = 0; i < childCollectionElements.getLength(); i++) {
            Element childCollectionElement = (Element) childCollectionElements.item(i);
            collection.addCollection(parseTreeCollection(childCollectionElement));
        }

        NodeList childPhotosetElements = collectionElement.getElementsByTagName("set");
        for (int i = 0; i < childPhotosetElements.getLength(); i++) {
            Element childPhotosetElement = (Element) childPhotosetElements.item(i);
            collection.addPhotoset(createPhotoset(childPhotosetElement));
        }

        return collection;
    }

    /**
     * 
     * @param childPhotosetElement
     * @return
     */
    private Photoset createPhotoset(Element childPhotosetElement) {
        Photoset photoset = new Photoset();
        photoset.setId(childPhotosetElement.getAttribute("id"));
        photoset.setTitle(childPhotosetElement.getAttribute("title"));
        photoset.setDescription(childPhotosetElement.getAttribute("description"));
        return photoset;
    }

    /**
     * 
     * @param collectionElement
     * @param collection
     */
    private void parseCommonFields(Element collectionElement, Collection collection) {
        collection.setId(collectionElement.getAttribute("id"));
        collection.setIconLarge(collectionElement.getAttribute("iconlarge"));
        collection.setIconSmall(collectionElement.getAttribute("iconsmall"));
    }
}
