package com.flickr4java.flickr.panda;

/**
 * Holds the name of a panda.
 * 
 * @author mago
 * @version $Id: Panda.java,v 1.3 2009/07/12 22:43:07 x-mago Exp $
 * @see com.flickr4java.flickr.panda.PandaInterface#getPhotos(Panda, java.util.Set, int, int)
 */
public class Panda {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
