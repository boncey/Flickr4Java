package com.flickr4java.flickr.commons;

import java.util.Date;

/**
 * A commons institution.
 * 
 * @author mago
 * @version $Id: Institution.java,v 1.2 2009/07/12 22:43:07 x-mago Exp $
 */
public class Institution {

    private String id;

    private String name;

    private Date dateLaunch;

    private String siteUrl;

    private String licenseUrl;

    private String flickrUrl;

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

    public Date getDateLaunch() {
        return dateLaunch;
    }

    public void setDateLaunch(Date dateLaunch) {
        this.dateLaunch = dateLaunch;
    }

    public void setDateLaunch(long date) {
        setDateLaunch(new Date(date));
    }

    public void setDateLaunch(String date) {
        if (date == null || "".equals(date))
            return;
        setDateLaunch(Long.parseLong(date) * 1000);
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getFlickrUrl() {
        return flickrUrl;
    }

    public void setFlickrUrl(String flickrUrl) {
        this.flickrUrl = flickrUrl;
    }

    @Override
    public String toString() {
        return String.format("Institution [name=%s]", name);
    }

}
