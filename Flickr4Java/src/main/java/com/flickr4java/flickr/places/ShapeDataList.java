package com.flickr4java.flickr.places;

import java.util.ArrayList;

public class ShapeDataList<E> extends ArrayList<ShapeData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3405746186476466870L;
	
	private int total;
	private String woeId;
	private String placeId;
	private String placeType;
	private int placeTypeId;
	
	
	public int getPlaceTypeId() {
		return placeTypeId;
	}
	
	public void setPlaceTypeId(int placeTypeId) {
		this.placeTypeId = placeTypeId;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getWoeId() {
		return woeId;
	}
	public void setWoeId(String woeId) {
		this.woeId = woeId;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

}
