package com.flickr4java.flickr.activity;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Gather activity information belonging to the calling user.
 * 
 * @author Martin Goebel
 * @version $Id: ActivityInterface.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class ActivityInterface {

    public static final String METHOD_USER_COMMENTS = "flickr.activity.userComments";

    public static final String METHOD_USER_PHOTOS = "flickr.activity.userPhotos";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public ActivityInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Returns a list of recent activity on photos commented on by the calling user.<br>
     * Flickr says: Do not poll this method more than once an hour.
     * 
     * @param perPage
     * @param page
     * @return ItemList
     * @throws FlickrException
     */
    public ItemList<Item> userComments(int perPage, int page) throws FlickrException {
        ItemList<Item> items = new ItemList<Item>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_USER_COMMENTS);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }

        if (page > 0) {
            parameters.put("page", "" + page);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element itemList = response.getPayload();
        NodeList itemElements = itemList.getElementsByTagName("item");
        items.setPage(itemList.getAttribute("page"));
        items.setPages(itemList.getAttribute("pages"));
        items.setPerPage(itemList.getAttribute("perpage"));
        items.setTotal(itemList.getAttribute("total"));

        for (int i = 0; i < itemElements.getLength(); i++) {
            Element itemElement = (Element) itemElements.item(i);
            items.add(createItem(itemElement));
        }

        return items;
    }

    /**
     * Returns a list of recent activity on photos belonging to the calling user.<br>
     * Flickr says: Do not poll this method more than once an hour.
     * 
     * @param perPage
     * @param page
     * @param timeframe
     * @return ItemList
     * @throws FlickrException
     */
    public ItemList<Item> userPhotos(int perPage, int page, String timeframe) throws FlickrException {
        ItemList<Item> items = new ItemList<Item>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_USER_PHOTOS);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }

        if (page > 0) {
            parameters.put("page", "" + page);
        }

        if (timeframe != null) {
            if (checkTimeframeArg(timeframe)) {
                parameters.put("timeframe", timeframe);
            } else {
                throw new FlickrException("0", "Timeframe-argument to getUserPhotos() not valid");
            }
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element itemList = response.getPayload();
        NodeList itemElements = itemList.getElementsByTagName("item");
        items.setPage(itemList.getAttribute("page"));
        items.setPages(itemList.getAttribute("pages"));
        items.setPerPage(itemList.getAttribute("perpage"));
        items.setTotal(itemList.getAttribute("total"));

        for (int i = 0; i < itemElements.getLength(); i++) {
            Element itemElement = (Element) itemElements.item(i);
            items.add(createItem(itemElement));
        }

        return items;
    }

    private Item createItem(Element itemElement) {
        Item item = new Item();
        item.setId(itemElement.getAttribute("id"));
        item.setSecret(itemElement.getAttribute("secret"));
        item.setType(itemElement.getAttribute("type"));
        item.setTitle(XMLUtilities.getChildValue(itemElement, "title"));
        item.setFarm(itemElement.getAttribute("farm"));
        item.setServer(itemElement.getAttribute("server"));
        // userComments
        try {
            item.setComments(XMLUtilities.getIntAttribute(itemElement, "comments"));
            item.setNotes(XMLUtilities.getIntAttribute(itemElement, "notes"));
        } catch (Exception e) {
        }
        // userPhotos
        try {
            item.setCommentsOld(XMLUtilities.getIntAttribute(itemElement, "commentsold"));
            item.setCommentsNew(XMLUtilities.getIntAttribute(itemElement, "commentsnew"));
            item.setNotesOld(XMLUtilities.getIntAttribute(itemElement, "notesold"));
            item.setNotesNew(XMLUtilities.getIntAttribute(itemElement, "notesnew"));
        } catch (Exception e) {
        }
        item.setViews(XMLUtilities.getIntAttribute(itemElement, "views"));
        item.setFaves(XMLUtilities.getIntAttribute(itemElement, "faves"));
        item.setMore(XMLUtilities.getIntAttribute(itemElement, "more"));

        try {
            Element activityElement = (Element) itemElement.getElementsByTagName("activity").item(0);
            List<Event> events = new ArrayList<Event>();
            NodeList eventNodes = activityElement.getElementsByTagName("event");
            for (int i = 0; i < eventNodes.getLength(); i++) {
                Element eventElement = (Element) eventNodes.item(i);
                Event event = new Event();
                event.setType(eventElement.getAttribute("type"));
                if (event.getType().equals("comment")) {
                    event.setId(eventElement.getAttribute("commentid"));
                } else if (event.getType().equals("note")) {
                    event.setId(eventElement.getAttribute("noteid"));
                } else if (event.getType().equals("fave")) {
                    // has no id
                }
                event.setUser(eventElement.getAttribute("user"));
                event.setUsername(eventElement.getAttribute("username"));
                event.setDateadded(eventElement.getAttribute("dateadded"));
                event.setValue(XMLUtilities.getValue(eventElement));
                events.add(event);
            }
            item.setEvents(events);
        } catch (NullPointerException e) {
            // nop
        }
        return item;
    }

    /**
     * Checks for a valid timeframe-argument.<br>
     * Expects either days, or hours. Like: 2d or 4h.
     * 
     * @param timeframe
     * @return boolean
     */
    public boolean checkTimeframeArg(String timeframe) {
        if (Pattern.compile("\\d*(d|h)", Pattern.CASE_INSENSITIVE).matcher(timeframe).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
