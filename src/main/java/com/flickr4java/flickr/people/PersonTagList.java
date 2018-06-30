package com.flickr4java.flickr.people;

import java.util.ArrayList;

public class PersonTagList<E> extends ArrayList<PersonTag> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1841985011941978229L;
	
	private int total;
	private int height;
	private int width;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	

}
