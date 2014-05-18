package com.flickr4java.flickr.photos.suggestions;

import java.util.ArrayList;

public class SuggestionList<E> extends ArrayList<Suggestion>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -791184866429373856L;
	private int total;
	private int perPage;
	private int page;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPerPage() {
		return perPage;
	}
	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

}
