package com.flickr4java.flickr.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

/**
 * Gather activity information belonging to the calling user.
 *
 * @author Martin Goebel
 * @version $Id: ActivityInterface.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class ActivityInterface {

    public static final String METHOD_USER_COMMENTS = "flickr.activity.userComments";
    public static final String METHOD_USER_PHOTOS = "flickr.activity.userPhotos";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public ActivityInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
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
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public ItemList userComments(int perPage, int page)
      throws IOException, SAXException, FlickrException {
        ItemList items = new ItemList();
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_USER_COMMENTS));
        parameters.add(new Parameter("api_key", apiKey));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }

        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public ItemList userPhotos(int perPage, int page, String timeframe)
      throws IOException, SAXException, FlickrException {
        ItemList items = new ItemList();
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_USER_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }

        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        if (timeframe != null) {
            if (checkTimeframeArg(timeframe)) {
                parameters.add(new Parameter("timeframe", timeframe));
            } else {
            	throw new FlickrException("0","Timeframe-argument to getUserPhotos() not valid");
            }
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
        } catch (Exception e) {}
        // userPhotos
        try {
            item.setCommentsOld(XMLUtilities.getIntAttribute(itemElement, "commentsold"));
            item.setCommentsNew(XMLUtilities.getIntAttribute(itemElement, "commentsnew"));
            item.setNotesOld(XMLUtilities.getIntAttribute(itemElement, "notesold"));
            item.setNotesNew(XMLUtilities.getIntAttribute(itemElement, "notesnew"));
        } catch (Exception e) {}
        item.setViews(XMLUtilities.getIntAttribute(itemElement, "views"));
        item.setFaves(XMLUtilities.getIntAttribute(itemElement, "faves"));
        item.setMore(XMLUtilities.getIntAttribute(itemElement, "more"));

        try {
            Element activityElement = (Element) itemElement.getElementsByTagName("activity").item(0);
            List events = new ArrayList();
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
        if (Pattern.compile("\\d*(d|h)", Pattern.CASE_INSENSITIVE)
          .matcher(timeframe).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
