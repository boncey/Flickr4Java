package com.flickr4java.flickr.places;

import com.flickr4java.flickr.SearchResultList;

/**
 * Simple Extension of SearchResultList.
 * 
 * @author mago
 * @version $Id: PlacesList.java,v 1.1 2008/01/19 22:53:56 x-mago Exp $
 */
public class PlacesList<E> extends SearchResultList<Place> {
    private static final long serialVersionUID = -6773614467896936754L;
    private String bBox;
    private String placeType;
    
    public PlacesList() {
    }

	public String getBBox() {
		return bBox;
	}

	public void setBBox(String bBox) {
		this.bBox = bBox;
	}

	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

	
}
