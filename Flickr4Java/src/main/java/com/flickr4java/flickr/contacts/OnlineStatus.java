/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.contacts;

import java.io.Serializable;

/**
 * Class representing the various types of online statuses.
 * 
 * @author Anthony Eden
 */
public class OnlineStatus implements Serializable {
    private static final long serialVersionUID = 12L;

    public static final int OFFLINE_TYPE = 0;

    public static final int AWAY_TYPE = 1;

    public static final int ONLINE_TYPE = 2;

    public static final int UNKNOWN_TYPE = 100;

    public static final OnlineStatus OFFLINE = new OnlineStatus(OFFLINE_TYPE);

    public static final OnlineStatus AWAY = new OnlineStatus(AWAY_TYPE);

    public static final OnlineStatus ONLINE = new OnlineStatus(ONLINE_TYPE);

    public static final OnlineStatus UNKNOWN = new OnlineStatus(UNKNOWN_TYPE);

    private int type;

    private OnlineStatus(int type) {
        this.type = type;
    }

    /**
     * Get the int value for the online status. This method is useful in switch statements.
     * 
     * @return The int value for the online status
     */
    public int getType() {
        return type;
    }

    /**
     * Get an OnlineStatus object for a given int value.
     * 
     * @param type
     *            The int value
     * @return The OnlineStatus object
     */
    public static OnlineStatus fromType(int type) {
        switch (type) {
        case OFFLINE_TYPE:
            return OFFLINE;
        case AWAY_TYPE:
            return AWAY;
        case ONLINE_TYPE:
            return ONLINE;
        case UNKNOWN_TYPE:
            return UNKNOWN;
        default:
            throw new IllegalArgumentException("Unsupported online type: " + type);
        }
    }

    /**
     * Get the OnlineStatus value for a given int represented as a String
     * 
     * @param type
     *            The int represented as a String
     * @return The OnlineStatus object
     */
    public static OnlineStatus fromType(String type) {
        if (type == null || "".equals(type)) {
            return UNKNOWN;
        } else {
            return fromType(Integer.parseInt(type));
        }
    }
}
