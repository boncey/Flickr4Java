/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;

/**
 * Interface for working with Flickr Groups.
 * 
 * @author Anthony Eden
 * @version $Id: GroupsInterface.java,v 1.19 2009/07/11 20:30:27 x-mago Exp $
 */
public class GroupsInterface {

    public static final String METHOD_BROWSE = "flickr.groups.browse";

    public static final String METHOD_GET_ACTIVE_LIST = "flickr.groups.getActiveList";

    public static final String METHOD_GET_INFO = "flickr.groups.getInfo";

    public static final String METHOD_SEARCH = "flickr.groups.search";

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

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
    public Category browse(String catId) throws FlickrException {
        List<Subcategory> subcategories = new ArrayList<Subcategory>();
        List<Group> groups = new ArrayList<Group>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_BROWSE);
        parameters.put(Flickr.API_KEY, apiKey);

        if (catId != null) {
            parameters.put("cat_id", catId);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
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
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("group_id", groupId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
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
            System.err.println("WARNING: more than one throttle element in group");
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
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("text", text);

        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
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
}
