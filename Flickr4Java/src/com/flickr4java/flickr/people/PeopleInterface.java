/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.people;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

/**
 * Interface for finding Flickr users.
 *
 * @author Anthony Eden
 * @version $Id: PeopleInterface.java,v 1.28 2010/09/12 20:13:57 x-mago Exp $
 */
public class PeopleInterface {

    public static final String METHOD_FIND_BY_EMAIL = "flickr.people.findByEmail";
    public static final String METHOD_FIND_BY_USERNAME = "flickr.people.findByUsername";
    public static final String METHOD_GET_INFO = "flickr.people.getInfo";
    public static final String METHOD_GET_ONLINE_LIST = "flickr.people.getOnlineList";
    public static final String METHOD_GET_PUBLIC_GROUPS = "flickr.people.getPublicGroups";
    public static final String METHOD_GET_PUBLIC_PHOTOS = "flickr.people.getPublicPhotos";
    public static final String METHOD_GET_UPLOAD_STATUS = "flickr.people.getUploadStatus";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PeopleInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Find the user by their email address.
     *
     * This method does not require authentication.
     *
     * @param email The email address
     * @return The User
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User findByEmail(String email) throws IOException, SAXException, FlickrException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("method", METHOD_FIND_BY_EMAIL);
        parameters.put("api_key", apiKey);

        parameters.put("find_email", email);

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Find a User by the username.
     *
     * This method does not require authentication.
     *
     * @param username The username
     * @return The User object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User findByUsername(String username) throws IOException, SAXException, FlickrException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("method", METHOD_FIND_BY_USERNAME);
        parameters.put("api_key", apiKey);

        parameters.put("username", username);

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Get info about the specified user.
     *
     * This method does not require authentication.
     *
     * @param userId The user ID
     * @return The User object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User getInfo(String userId) throws IOException, SAXException, FlickrException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("method", METHOD_GET_INFO);
        parameters.put("api_key", apiKey);

        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setAdmin("1".equals(userElement.getAttribute("isadmin")));
        user.setPro("1".equals(userElement.getAttribute("ispro")));
        user.setIconFarm(userElement.getAttribute("iconfarm"));
        user.setIconServer(userElement.getAttribute("iconserver"));
        user.setRevContact("1".equals(userElement.getAttribute("revcontact")));
        user.setRevFriend("1".equals(userElement.getAttribute("revfriend")));
        user.setRevFamily("1".equals(userElement.getAttribute("revfamily")));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        user.setRealName(XMLUtilities.getChildValue(userElement, "realname"));
        user.setLocation(XMLUtilities.getChildValue(userElement, "location"));
        user.setMbox_sha1sum(XMLUtilities.getChildValue(userElement, "mbox_sha1sum"));
        user.setPhotosurl(XMLUtilities.getChildValue(userElement, "photosurl"));
        user.setProfileurl(XMLUtilities.getChildValue(userElement, "profileurl"));
        user.setMobileurl(XMLUtilities.getChildValue(userElement, "mobileurl"));

        Element photosElement = XMLUtilities.getChild(userElement, "photos");
        user.setPhotosFirstDate(XMLUtilities.getChildValue(photosElement, "firstdate"));
        user.setPhotosFirstDateTaken(XMLUtilities.getChildValue(photosElement, "firstdatetaken"));
        user.setPhotosCount(XMLUtilities.getChildValue(photosElement, "count"));

        return user;
    }

    /**
     * Get a collection of public groups for the user.
     *
     * The groups will contain only the members nsid, name, admin and eighteenplus.
     * If you want the whole group-information, you have to call 
     * {@link com.flickr4java.flickr.test.groups.GroupsInterface#getInfo(String)}.
     *
     * This method does not require authentication.
     *
     * @param userId The user ID
     * @return The public groups
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Collection getPublicGroups(String userId)
      throws IOException, SAXException, FlickrException {
        List groups = new ArrayList();

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("method", METHOD_GET_PUBLIC_GROUPS);
        parameters.put("api_key", apiKey);

        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element groupsElement = response.getPayload();
        NodeList groupNodes = groupsElement.getElementsByTagName("group");
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Element groupElement = (Element) groupNodes.item(i);
            Group group = new Group();
            group.setId(groupElement.getAttribute("nsid"));
            group.setName(groupElement.getAttribute("name"));
            group.setAdmin("1".equals(groupElement.getAttribute("admin")));
            group.setEighteenPlus(groupElement.getAttribute("eighteenplus").equals("0") ? false : true);
            groups.add(group);
        }
        return groups;
    }

    public PhotoList getPublicPhotos(String userId, int perPage, int page)
    throws IOException, SAXException, FlickrException {
        return getPublicPhotos(userId, Extras.MIN_EXTRAS, perPage, page);
    }

    /**
     * Get a collection of public photos for the specified user ID.
     *
     * This method does not require authentication.
     *
     * @see com.flickr4java.flickr.test.photos.Extras
     * @param userId The User ID
     * @param extras Set of extra-attributes to include (may be null)
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return The PhotoList collection
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getPublicPhotos(String userId, Set extras, int perPage, int page) throws IOException, SAXException,
            FlickrException {
        PhotoList photos = new PhotoList();

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("method", METHOD_GET_PUBLIC_PHOTOS);
        parameters.put("api_key", apiKey);

        parameters.put("user_id", userId);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }

        if (extras != null) {
            parameters.put(Extras.KEY_EXTRAS, StringUtilities.join(extras, ","));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
     * Get upload status for the currently authenticated user.
     *
     * Requires authentication with 'read' permission using the new authentication API.
     *
     * @return A User object with upload status data fields filled
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User getUploadStatus() throws IOException, SAXException, FlickrException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("method", METHOD_GET_UPLOAD_STATUS);
        parameters.put("api_key", apiKey);

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("id"));
        user.setPro("1".equals(userElement.getAttribute("ispro")));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));

        Element bandwidthElement = XMLUtilities.getChild(userElement, "bandwidth");
        user.setBandwidthMax(bandwidthElement.getAttribute("max"));
        user.setBandwidthUsed(bandwidthElement.getAttribute("used"));

        Element filesizeElement = XMLUtilities.getChild(userElement, "filesize");
        user.setFilesizeMax(filesizeElement.getAttribute("max"));

        return user;
    }
}
