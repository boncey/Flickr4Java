/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum class for Permissions defined for auth results.
 * 
 * @author Anthony Eden
 * 
 *         TODO Make into a proper enum.
 */
public class Permission implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5384461370301078353L;

    /**
     * Type value for no permissions If no permissions are requested, the application will only display public photos.
     */
    public static final int NONE_TYPE = 0;

    /**
     * Type value for Read Permission. You will be able to see all your photos via the application. (This includes your private photos.)
     */
    public static final int READ_TYPE = 1;

    /**
     * Type value for Write Permission (and Read). You will be able to see all your photos, upload new photos, and add, edit or delete photo metadata (titles,
     * descriptions, tags, etc.).
     */
    public static final int WRITE_TYPE = 2;

    /**
     * Type value for Delete Permission (and Write, Read). You are able to delete Flickr photos via the application.
     */
    public static final int DELETE_TYPE = 3;

    /**
     * No permissions. If no permissions are requested, the application will only display public photos.
     */
    public static final Permission NONE = new Permission(NONE_TYPE);

    /**
     * Read Permission. You will be able to see all your photos via the application. (This includes your private photos.)
     */
    public static final Permission READ = new Permission(READ_TYPE);

    /**
     * Write (and Read). You will be able to see all your photos, upload new photos, and add, edit or delete photo metadata (titles, descriptions, tags, etc.).
     */
    public static final Permission WRITE = new Permission(WRITE_TYPE);

    /**
     * Delete (and Write, Read). You are able to delete Flickr photos via the application.
     */
    public static final Permission DELETE = new Permission(DELETE_TYPE);

    private static final Map<String, Permission> stringToPermissionMap = new HashMap<String, Permission>();
    static {
        stringToPermissionMap.put("none", NONE);
        stringToPermissionMap.put("read", READ);
        stringToPermissionMap.put("write", WRITE);
        stringToPermissionMap.put("delete", DELETE);
    }

    private final int type;

    private Permission(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * Convert the permission String to a Permission object.
     * 
     * @param permission
     *            The permission String
     * @return The Permission object
     */
    public static Permission fromString(String permission) {
        return (Permission) stringToPermissionMap.get(permission.toLowerCase());
    }

    @Override
    public String toString() {
        switch (type) {
        case NONE_TYPE:
            return "none";
        case READ_TYPE:
            return "read";
        case WRITE_TYPE:
            return "write";
        case DELETE_TYPE:
            return "delete";
        default:
            throw new IllegalStateException("Unsupported type: " + type);
        }
    }

}
