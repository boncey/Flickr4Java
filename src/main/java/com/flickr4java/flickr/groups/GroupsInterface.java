/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.groups;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for working with Flickr Groups.
 * 
 * @author Anthony Eden
 * @version $Id: GroupsInterface.java,v 1.19 2009/07/11 20:30:27 x-mago Exp $
 */
public class GroupsInterface {

    private static Logger _log = LoggerFactory.getLogger(GroupsInterface.class);

    public static final String METHOD_BROWSE = "flickr.groups.browse";

    public static final String METHOD_GET_ACTIVE_LIST = "flickr.groups.getActiveList";

    public static final String METHOD_GET_INFO = "flickr.groups.getInfo";

    public static final String METHOD_SEARCH = "flickr.groups.search";

    public static final String METHOD_JOIN = "flickr.groups.join";

    public static final String METHOD_JOIN_REQUEST = "flickr.groups.joinRequest";

    public static final String METHOD_LEAVE = "flickr.groups.leave";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public GroupsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Browse groups for the given category ID. If a null value is passed for the category then the root category is used.
     * 
     * @param catId
     *            The optional category id. Null value will be ignored.
     * @return The Collection of Photo objects
     * @throws FlickrException
     * @deprecated Flickr returns just empty results
     */
    @Deprecated
    public Category browse(String catId) throws FlickrException {
        List<Subcategory> subcategories = new ArrayList<Subcategory>();
        List<Group> groups = new ArrayList<Group>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_BROWSE);

        if (catId != null) {
            parameters.put("cat_id", catId);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element categoryElement = response.getPayload();

        Category category = new Category();
        category.setName(categoryElement.getAttribute("name"));
        category.setPath(categoryElement.getAttribute("path"));
        category.setPathIds(categoryElement.getAttribute("pathids"));

        NodeList subcatNodes = categoryElement.getElementsByTagName("subcat");
        for (int i = 0; i < subcatNodes.getLength(); i++) {
            Element node = (Element) subcatNodes.item(i);
            Subcategory subcategory = new Subcategory();
            subcategory.setId(Integer.parseInt(node.getAttribute("id")));
            subcategory.setName(node.getAttribute("name"));
            subcategory.setCount(Integer.parseInt(node.getAttribute("count")));

            subcategories.add(subcategory);
        }

        NodeList groupNodes = categoryElement.getElementsByTagName("group");
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Element node = (Element) groupNodes.item(i);
            Group group = new Group();
            group.setId(node.getAttribute("nsid"));
            group.setName(node.getAttribute("name"));
            group.setMembers(node.getAttribute("members"));

            groups.add(group);
        }

        category.setGroups(groups);
        category.setSubcategories(subcategories);

        return category;
    }

    /**
     * Get the info for a specified group.
     * 
     * This method does not require authentication.
     * 
     * @param groupId
     *            The group id
     * @return The Group object
     */
    public Group getInfo(String groupId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);
        parameters.put("group_id", groupId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element groupElement = response.getPayload();
        Group group = new Group();
        group.setId(groupElement.getAttribute("id"));
        group.setIconFarm(groupElement.getAttribute("iconfarm"));
        group.setIconServer(groupElement.getAttribute("iconserver"));
        group.setLang(groupElement.getAttribute("lang"));
        group.setPoolModerated(groupElement.getAttribute("ispoolmoderated").equals("0") ? false : true);

        group.setName(XMLUtilities.getChildValue(groupElement, "name"));
        group.setDescription(XMLUtilities.getChildValue(groupElement, "description"));
        group.setMembers(XMLUtilities.getChildValue(groupElement, "members"));
        group.setPrivacy(XMLUtilities.getChildValue(groupElement, "privacy"));
        group.setPoolCount(XMLUtilities.getChildValue(groupElement, "pool_count"));
        group.setTopicCount(XMLUtilities.getChildValue(groupElement, "topic_count"));

        NodeList throttleNodes = groupElement.getElementsByTagName("throttle");
        int n = throttleNodes.getLength();
        if (n == 1) {
            Element throttleElement = (Element) throttleNodes.item(0);
            Throttle throttle = new Throttle();
            group.setThrottle(throttle);
            throttle.setMode(throttleElement.getAttribute("mode"));
            String countStr = throttleElement.getAttribute("count");
            String remainingStr = throttleElement.getAttribute("remaining");
            if (countStr != null && countStr.length() > 0) {
                throttle.setCount(Integer.parseInt(countStr));
            }
            if (remainingStr != null && remainingStr.length() > 0) {
                throttle.setRemaining(Integer.parseInt(remainingStr));
            }
        } else if (n > 1) {
            _log.warn("WARNING: more than one throttle element in group");
        }

        NodeList restrictionNodes = groupElement.getElementsByTagName("restrictions");
        n = restrictionNodes.getLength();
        if (n == 1) {
            Element restrictionElement = (Element) restrictionNodes.item(0);
            Restriction restriction = new Restriction();
            group.setRestriction(restriction);
            restriction.setIsPhotosOk("1".equals(restrictionElement.getAttribute("photos_ok")));
            restriction.setIsVideosOk("1".equals(restrictionElement.getAttribute("videos_ok")));
            restriction.setIsImagesOk("1".equals(restrictionElement.getAttribute("images_ok")));
            restriction.setIsScreensOk("1".equals(restrictionElement.getAttribute("screens_ok")));
            restriction.setIsArtOk("1".equals(restrictionElement.getAttribute("art_ok")));
            restriction.setIsSafeOk("1".equals(restrictionElement.getAttribute("safe_ok")));
            restriction.setIsModerateOk("1".equals(restrictionElement.getAttribute("moderate_ok")));
            restriction.setIsRestrictedOk("1".equals(restrictionElement.getAttribute("restricted_ok")));
            restriction.setIsHasGeo("1".equals(restrictionElement.getAttribute("has_geo")));
        } else if (n > 1) {
            _log.warn("WARNING: more than one throttle element in group");
        }
        NodeList blastNodes = groupElement.getElementsByTagName("blast");
        n = blastNodes.getLength();
        if (n == 1) {
            Element blastElement = (Element) blastNodes.item(0);
            Blast blast = new Blast();
            group.setBlast(blast);
            blast.setUserId(blastElement.getAttribute("user_id"));
            blast.setDateBlastAdded(blastElement.getAttribute("date_blast_added"));
            blast.setBlast(XMLUtilities.getChildValue(groupElement, "blast"));
        } else if (n > 1) {
            _log.warn("WARNING: more than one throttle element in group");
        }

        return group;
    }

    /**
     * Search for groups. 18+ groups will only be returned for authenticated calls where the authenticated user is over 18. This method does not require
     * authentication.
     * 
     * @param text
     *            The text to search for.
     * @param perPage
     *            Number of groups to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page
     *            The page of results to return. If this argument is 0, it defaults to 1.
     * @return A GroupList Object. Only the fields <em>id</em>, <em>name</em> and <em>eighteenplus</em> in the Groups will be set.
     * @throws FlickrException
     */
    public Collection<Group> search(String text, int perPage, int page) throws FlickrException {
        GroupList<Group> groupList = new GroupList<Group>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SEARCH);

        parameters.put("text", text);

        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element groupsElement = response.getPayload();
        NodeList groupNodes = groupsElement.getElementsByTagName("group");
        groupList.setPage(XMLUtilities.getIntAttribute(groupsElement, "page"));
        groupList.setPages(XMLUtilities.getIntAttribute(groupsElement, "pages"));
        groupList.setPerPage(XMLUtilities.getIntAttribute(groupsElement, "perpage"));
        groupList.setTotal(XMLUtilities.getIntAttribute(groupsElement, "total"));
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Element groupElement = (Element) groupNodes.item(i);
            Group group = new Group();
            group.setId(groupElement.getAttribute("nsid"));
            group.setName(groupElement.getAttribute("name"));
            groupList.add(group);
        }
        return groupList;
    }

    /**
     * Join a group as a public member.
     * 
     * Note: if a group has rules - the client must display the rules to the user and the user must accept them prior to joining the group. The acceptRules
     * parameter indicates that the user has accepted those rules.
     * 
     * @param groupId
     *            - the id of the group to join
     * @param acceptRules
     *            - if a group has rules, true indicates the user has accepted the rules
     * 
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.join.html">flickr.groups.join</a>
     */
    public void join(String groupId, Boolean acceptRules) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_JOIN);
        parameters.put("group_id", groupId);
        if (acceptRules != null) {
            parameters.put("accept_rules", acceptRules);
        }

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Request to join a group.
     * 
     * Note: if a group has rules, the client must display the rules to the user and the user must accept them (which is indicated by passing a true value to
     * acceptRules) prior to making the join request.
     * 
     * @param groupId
     *            - groupId parameter
     * @param message
     *            - (required) message to group administrator
     * @param acceptRules
     *            - (required) parameter indicating user has accepted groups rules
     */
    public void joinRequest(String groupId, String message, boolean acceptRules) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_JOIN_REQUEST);
        parameters.put("group_id", groupId);
        parameters.put("message", message);
        parameters.put("accept_rules", acceptRules);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Leave a group.
     * 
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.leave.html">lickr.groups.leave</a> for a description of the various behaviors possible
     *      when a user leaves a group.
     * 
     * @param groupId
     *            - the id of the group to leave
     * @param deletePhotos
     *            - delete photos by this user from group
     */
    public void leave(String groupId, Boolean deletePhotos) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_LEAVE);
        parameters.put("group_id", groupId);
        parameters.put("delete_photos", deletePhotos);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }
}
