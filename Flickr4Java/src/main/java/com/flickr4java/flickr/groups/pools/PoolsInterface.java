/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.groups.pools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;

/**
 * @author Anthony Eden
 * @version $Id: PoolsInterface.java,v 1.16 2011/07/02 19:00:59 x-mago Exp $
 */
public class PoolsInterface {

    public static final String METHOD_ADD = "flickr.groups.pools.add";

    public static final String METHOD_GET_CONTEXT = "flickr.groups.pools.getContext";

    public static final String METHOD_GET_GROUPS = "flickr.groups.pools.getGroups";

    public static final String METHOD_GET_PHOTOS = "flickr.groups.pools.getPhotos";

    public static final String METHOD_REMOVE = "flickr.groups.pools.remove";

    private String apiKey;

    private String sharedSecret;

    private Transport transport;

    public PoolsInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Add a photo to a group's pool.
     * 
     * @param photoId
     *            The photo ID
     * @param groupId
     *            The group ID
     */
    public void add(String photoId, String groupId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("group_id", groupId);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Get the context for a photo in the group pool.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo ID
     * @param groupId
     *            The group ID
     * @return The PhotoContext
     * @throws FlickrException
     */
    public PhotoContext getContext(String photoId, String groupId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTEXT);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("group_id", groupId);

        Response response = transport.get(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Collection<Element> payload = response.getPayloadCollection();
        PhotoContext photoContext = new PhotoContext();
        for (Element element : payload) {
            String elementName = element.getTagName();
            if (elementName.equals("prevphoto")) {
                Photo photo = new Photo();
                photo.setId(element.getAttribute("id"));
                photoContext.setPreviousPhoto(photo);
            } else if (elementName.equals("nextphoto")) {
                Photo photo = new Photo();
                photo.setId(element.getAttribute("id"));
                photoContext.setNextPhoto(photo);
            } else {
                System.err.println("unsupported element name: " + elementName);
            }
        }
        return photoContext;
    }

    /**
     * Get a collection of all of the user's groups.
     * 
     * @return A Collection of Group objects
     * @throws FlickrException
     */
    public Collection<Group> getGroups() throws FlickrException {
        List<Group> groups = new ArrayList<Group>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_GROUPS);
        parameters.put(Flickr.API_KEY, apiKey);

        Response response = transport.get(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element groupsElement = response.getPayload();
        NodeList groupNodes = groupsElement.getElementsByTagName("group");
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Element groupElement = (Element) groupNodes.item(i);
            Group group = new Group();
            group.setId(groupElement.getAttribute("id"));
            group.setName(groupElement.getAttribute("name"));
            group.setAdmin("1".equals(groupElement.getAttribute("admin")));
            group.setPrivacy(groupElement.getAttribute("privacy"));
            groups.add(group);
        }
        return groups;
    }

    /**
     * Get the photos for the specified group pool, optionally filtering by taf.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param groupId
     *            The group ID
     * @param tags
     *            The optional tags (may be null)
     * @param extras
     *            Set of extra-attributes to include (may be null)
     * @param perPage
     *            The number of photos per page (0 to ignore)
     * @param page
     *            The page offset (0 to ignore)
     * @return A Collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getPhotos(String groupId, String[] tags, Set<String> extras, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PHOTOS);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("group_id", groupId);
        if (tags != null) {
            parameters.put("tags", StringUtilities.join(tags, " "));
        }
        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        if (extras != null) {
            StringBuffer sb = new StringBuffer();
            Iterator<String> it = extras.iterator();
            for (int i = 0; it.hasNext(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(it.next());
            }
            parameters.put(Extras.KEY_EXTRAS, sb.toString());
        }

        Response response = transport.get(transport.getPath(), parameters, sharedSecret);
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
            photos.add(PhotoUtils.createPhoto(photoElement));
        }

        return photos;
    }

    /**
     * Convenience/Compatibility method.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param groupId
     *            The group ID
     * @param tags
     *            The optional tags (may be null)
     * @param perPage
     *            The number of photos per page (0 to ignore)
     * @param page
     *            The page offset (0 to ignore)
     * @return A Collection of Photo objects
     */
    public PhotoList<Photo> getPhotos(String groupId, String[] tags, int perPage, int page) throws FlickrException {
        return getPhotos(groupId, tags, Extras.MIN_EXTRAS, perPage, page);
    }

    /**
     * Remove the specified photo from the group.
     * 
     * @param photoId
     *            The photo ID
     * @param groupId
     *            The group ID
     * @throws FlickrException
     */
    public void remove(String photoId, String groupId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REMOVE);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("group_id", groupId);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
