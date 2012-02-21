package com.flickr4java.flickr.util;

/**
 * Defines an interface for methods needed to get a BuddyIconUrl.
 * 
 * @author mago
 * @version $Id: BuddyIconable.java,v 1.1 2008/01/11 21:02:55 x-mago Exp $
 */
public interface BuddyIconable {
    String getBuddyIconUrl();

    int getIconFarm();

    int getIconServer();

    void setIconFarm(int iconFarm);

    void setIconFarm(String iconFarm);

    void setIconServer(int iconServer);

    void setIconServer(String iconServer);
}
