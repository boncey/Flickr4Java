/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.contacts;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for working with Flickr contacts.
 * 
 * @author Anthony Eden
 * @version $Id: ContactsInterface.java,v 1.18 2009/07/11 20:30:27 x-mago Exp $
 */
public class ContactsInterface {

    private static final String METHOD_GET_LIST = "flickr.contacts.getList";

    private static final String METHOD_GET_LIST_RECENTLY_UPLOADED = "flickr.contacts.getListRecentlyUploaded";

    private static final String METHOD_GET_PUBLIC_LIST = "flickr.contacts.getPublicList";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public ContactsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get the collection of contacts for the calling user.
     * 
     * @return The Collection of Contact objects
     */
    public Collection<Contact> getList() throws FlickrException {
    	 ContactList<Contact> contacts = new ContactList<Contact>();
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element contactsElement = response.getPayload();
        contacts.setPage(contactsElement.getAttribute("page"));
        contacts.setPages(contactsElement.getAttribute("pages"));
        contacts.setPerPage(contactsElement.getAttribute("perpage"));
        contacts.setTotal(contactsElement.getAttribute("total"));
        NodeList contactNodes = contactsElement.getElementsByTagName("contact");
        for (int i = 0; i < contactNodes.getLength(); i++) {
            Element contactElement = (Element) contactNodes.item(i);
            Contact contact = new Contact();
            contact.setId(contactElement.getAttribute("nsid"));
            contact.setUsername(contactElement.getAttribute("username"));
            contact.setRealName(contactElement.getAttribute("realname"));
            contact.setFriend("1".equals(contactElement.getAttribute("friend")));
            contact.setFamily("1".equals(contactElement.getAttribute("family")));
            contact.setIgnored("1".equals(contactElement.getAttribute("ignored")));
            String lPathAlias = contactElement.getAttribute("path_alias");
            contact.setPathAlias(lPathAlias == null || "".equals(lPathAlias) ? null : lPathAlias);
            contact.setOnline(OnlineStatus.fromType(contactElement.getAttribute("online")));
            contact.setIconFarm(contactElement.getAttribute("iconfarm"));
            contact.setIconServer(contactElement.getAttribute("iconserver"));
            if (contact.getOnline() == OnlineStatus.AWAY) {
                contactElement.normalize();
                contact.setAwayMessage(XMLUtilities.getValue(contactElement));
            }
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * Return a list of contacts for a user who have recently uploaded photos along with the total count of photos uploaded.
     * 
     * @param lastUpload
     *            Limits the resultset to contacts that have uploaded photos since this date. The date should be in the form of a Unix timestamp. The default,
     *            and maximum, offset is (1) hour. (Optional, can be null)
     * @param filter
     *            Limit the result set to all contacts or only those who are friends or family.<br/>
     *            Valid options are: <b>ff</b> -&gt; friends and family, <b>all</b> -&gt; all your contacts. (Optional, can be null)
     * 
     * @return List of Contacts
     * @throws FlickrException
     */
    public Collection<Contact> getListRecentlyUploaded(Date lastUpload, String filter) throws FlickrException {
        List<Contact> contacts = new ArrayList<Contact>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST_RECENTLY_UPLOADED);

        if (lastUpload != null) {
            parameters.put("date_lastupload", String.valueOf(lastUpload.getTime() / 1000L));
        }
        if (filter != null) {
            parameters.put("filter", filter);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element contactsElement = response.getPayload();
        NodeList contactNodes = contactsElement.getElementsByTagName("contact");
        for (int i = 0; i < contactNodes.getLength(); i++) {
            Element contactElement = (Element) contactNodes.item(i);
            Contact contact = new Contact();
            contact.setId(contactElement.getAttribute("nsid"));
            contact.setUsername(contactElement.getAttribute("username"));
            contact.setRealName(contactElement.getAttribute("realname"));
            contact.setFriend("1".equals(contactElement.getAttribute("friend")));
            contact.setFamily("1".equals(contactElement.getAttribute("family")));
            contact.setIgnored("1".equals(contactElement.getAttribute("ignored")));
            contact.setOnline(OnlineStatus.fromType(contactElement.getAttribute("online")));
            contact.setIconFarm(contactElement.getAttribute("iconfarm"));
            contact.setIconServer(contactElement.getAttribute("iconserver"));
            if (contact.getOnline() == OnlineStatus.AWAY) {
                contactElement.normalize();
                contact.setAwayMessage(XMLUtilities.getValue(contactElement));
            }
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * Get the collection of public contacts for the specified user ID.
     * 
     * This method does not require authentication.
     * 
     * @param userId
     *            The user ID
     * @return The Collection of Contact objects
     * @throws FlickrException
     */
    public Collection<Contact> getPublicList(String userId) throws FlickrException {
        List<Contact> contacts = new ArrayList<Contact>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PUBLIC_LIST);
        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element contactsElement = response.getPayload();
        NodeList contactNodes = contactsElement.getElementsByTagName("contact");
        for (int i = 0; i < contactNodes.getLength(); i++) {
            Element contactElement = (Element) contactNodes.item(i);
            Contact contact = new Contact();
            contact.setId(contactElement.getAttribute("nsid"));
            contact.setUsername(contactElement.getAttribute("username"));
            contact.setIgnored("1".equals(contactElement.getAttribute("ignored")));
            contact.setOnline(OnlineStatus.fromType(contactElement.getAttribute("online")));
            contact.setIconFarm(contactElement.getAttribute("iconfarm"));
            contact.setIconServer(contactElement.getAttribute("iconserver"));
            if (contact.getOnline() == OnlineStatus.AWAY) {
                contactElement.normalize();
                contact.setAwayMessage(XMLUtilities.getValue(contactElement));
            }
            contacts.add(contact);
        }
        return contacts;
    }

}
