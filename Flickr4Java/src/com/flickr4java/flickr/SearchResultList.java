/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr;

import java.util.ArrayList;

/**
 * Search result list with additional meta data.
 * 
 * @author Anthony Eden
 * @version $Id: SearchResultList.java,v 1.3 2007/07/20 19:06:27 x-mago Exp $
 */
public class SearchResultList<E> extends ArrayList<E> {

    private static final long serialVersionUID = -7962319033867024935L;

    private int page;

    private int pages;

    private int perPage;

    private int total;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPage(String page) {
        if (page != null && page.length() != 0) {
            setPage(Integer.parseInt(page));
        }
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setPages(String pages) {
        if (pages != null && pages.length() != 0) {
            setPages(Integer.parseInt(pages));
        }
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public void setPerPage(String perPage) {
        if (perPage != null && perPage.length() != 0) {
            setPerPage(Integer.parseInt(perPage));
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotal(String total) {
        if (total != null && total.length() != 0) {
            setTotal(Integer.parseInt(total));
        }
    }

}
