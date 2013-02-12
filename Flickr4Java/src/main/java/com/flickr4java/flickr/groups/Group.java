/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.groups;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.util.BuddyIconable;
import com.flickr4java.flickr.util.UrlUtilities;

/**
 * Class representing a Flickr Group.
 * 
 * @author Anthony Eden
 */
public class Group implements BuddyIconable {

    private String id;

    private String name;

    private int members;

    private String privacy;

    private int iconFarm;

    private int iconServer;

    private String description;

    private Throttle throttle;

    private String lang;

    private boolean poolModerated;

    private boolean invitationOnly;

    // the following seem not to exist anymore
    private int online;

    private String chatId;

    private int inChat;

    private boolean admin;

    private int photoCount;

    private boolean eighteenPlus;

    public Group() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public void setMembers(String members) {
        try {
            if (members != null)
                setMembers(Integer.parseInt(members));
        } catch (NumberFormatException nfe) {
            setMembers(0);
            if (Flickr.tracing)
                System.out.println("trace: Group.setMembers(String) encountered a number format " + "exception.  members set to 0");
        }
    }

    /**
     * @deprecated
     * @return the online-state
     */
    public int getOnline() {
        return online;
    }

    /**
     * @deprecated
     * @param online
     */
    public void setOnline(int online) {
        this.online = online;
    }

    /**
     * @deprecated
     * @param online
     */
    public void setOnline(String online) {
        try {
            if (online != null)
                setOnline(Integer.parseInt(online));
        } catch (NumberFormatException nfe) {
            setOnline(0);
            if (Flickr.tracing)
                System.out.println("trace: Group.setOnline(String) encountered a number format " + "exception.  online set to 0");
        }
    }

    /**
     * @deprecated
     * @return chatId
     */
    public String getChatId() {
        return chatId;
    }

    /**
     * @deprecated
     * @param chatId
     */
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    /**
     * @deprecated
     * @return the number of users in chat
     */
    public int getInChat() {
        return inChat;
    }

    /**
     * @deprecated
     * @param inChat
     */
    public void setInChat(int inChat) {
        this.inChat = inChat;
    }

    /**
     * @deprecated
     * @param inChat
     */
    public void setInChat(String inChat) {
        try {
            if (inChat != null)
                setInChat(Integer.parseInt(inChat));
        } catch (NumberFormatException nfe) {
            setInChat(0);
            if (Flickr.tracing) {
                System.out.println("trace: Group.setInChat(String) encountered a number format " + "exception.  InChat set to 0");
            }
        }
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    /**
     * @deprecated
     * @param photoCount
     */
    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    /**
     * @deprecated
     * @param photoCount
     */
    public void setPhotoCount(String photoCount) {
        if (photoCount != null) {
            try {
                setPhotoCount(Integer.parseInt(photoCount));
            } catch (NumberFormatException nfe) {
                setPhotoCount(0);
                if (Flickr.tracing) {
                    System.out.println("trace: Group.setPhotoCount(String) encountered a number format " + "exception.  PhotoCount set to 0");
                }
            }
        }
    }

    /**
     * @deprecated
     * @return boolean
     */
    public boolean isEighteenPlus() {
        return eighteenPlus;
    }

    /**
     * @deprecated
     * @param eighteenPlus
     */
    public void setEighteenPlus(boolean eighteenPlus) {
        this.eighteenPlus = eighteenPlus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isPoolModerated() {
        return poolModerated;
    }

    public void setPoolModerated(boolean poolModerated) {
        this.poolModerated = poolModerated;
    }

    public int getIconFarm() {
        return iconFarm;
    }

    public void setIconFarm(int iconFarm) {
        this.iconFarm = iconFarm;
    }

    public void setIconFarm(String iconFarm) {
        if (iconFarm != null) {
            setIconFarm(Integer.parseInt(iconFarm));
        }
    }

    public int getIconServer() {
        return iconServer;
    }

    public void setIconServer(int iconServer) {
        this.iconServer = iconServer;
    }

    public void setIconServer(String iconServer) {
        if (iconServer != null) {
            setIconServer(Integer.parseInt(iconServer));
        }
    }

    /**
     * Construct the BuddyIconUrl.
     * <p>
     * If none available, return the <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @return The BuddyIconUrl
     */
    public String getBuddyIconUrl() {
        return UrlUtilities.createBuddyIconUrl(iconFarm, iconServer, id);
    }

    public Throttle getThrottle() {
        return throttle;
    }

    public void setThrottle(Throttle throttle) {
        this.throttle = throttle;
    }

    /**
     * @return the invitationOnly
     */
    public boolean isInvitationOnly() {
        return invitationOnly;
    }

    /**
     * @param invitationOnly
     *            the invitationOnly to set
     */
    public void setInvitationOnly(boolean invitationOnly) {
        this.invitationOnly = invitationOnly;
    }

}
