/**
 * @author acaplan
 */
package com.flickr4java.flickr.galleries;

import com.flickr4java.flickr.people.User;

/**
 * @author acaplan
 * 
 */
public class Gallery {

    private String strCreateDate;

    private String strUpdateDate;

    private String strPrimaryPhotoId;

    private String strPrimaryPhotoServer;

    private String strPrimaryPhotoFarm;

    private String strPrimaryPhotoSecret;

    private String strPhotoCount;

    private String strVideoCount;

    private String strTitle;

    private String strDesc;

    private String strUrl;

    private User owner;

    private String id;

    public Gallery() {
    }

    /**
     * @return the strTitle
     */
    public String getTitle() {
        return strTitle;
    }

    /**
     * @param strTitle
     *            the strTitle to set
     */
    public void setTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    /**
     * @return the strDesc
     */
    public String getDesc() {
        return strDesc;
    }

    /**
     * @param strDesc
     *            the strDesc to set
     */
    public void setDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    /**
     * @return the strUrl
     */
    public String getUrl() {
        return strUrl;
    }

    /**
     * @param strUrl
     *            the strUrl to set
     */
    public void setUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    /**
     * @return the strCreateDate
     */
    public String getCreateDate() {
        return strCreateDate;
    }

    /**
     * @param strCreateDate
     *            the strCreateDate to set
     */
    public void setCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    /**
     * @return the strUpdateDate
     */
    public String getUpdateDate() {
        return strUpdateDate;
    }

    /**
     * @param strUpdateDate
     *            the strUpdateDate to set
     */
    public void setUpdateDate(String strUpdateDate) {
        this.strUpdateDate = strUpdateDate;
    }

    /**
     * @return the strPrimaryPhotoId
     */
    public String getPrimaryPhotoId() {
        return strPrimaryPhotoId;
    }

    /**
     * @param strPrimaryPhotoId
     *            the strPrimaryPhotoId to set
     */
    public void setPrimaryPhotoId(String strPrimaryPhotoId) {
        this.strPrimaryPhotoId = strPrimaryPhotoId;
    }

    /**
     * @return the strPrimaryPhotoServer
     */
    public String getPrimaryPhotoServer() {
        return strPrimaryPhotoServer;
    }

    /**
     * @param strPrimaryPhotoServer
     *            the strPrimaryPhotoServer to set
     */
    public void setPrimaryPhotoServer(String strPrimaryPhotoServer) {
        this.strPrimaryPhotoServer = strPrimaryPhotoServer;
    }

    /**
     * @return the strPrimaryPhotoFarm
     */
    public String getPrimaryPhotoFarm() {
        return strPrimaryPhotoFarm;
    }

    /**
     * @param strPrimaryPhotoFarm
     *            the strPrimaryPhotoFarm to set
     */
    public void setPrimaryPhotoFarm(String strPrimaryPhotoFarm) {
        this.strPrimaryPhotoFarm = strPrimaryPhotoFarm;
    }

    /**
     * @return the strPrimaryPhotoSecret
     */
    public String getPrimaryPhotoSecret() {
        return strPrimaryPhotoSecret;
    }

    /**
     * @param strPrimaryPhotoSecret
     *            the strPrimaryPhotoSecret to set
     */
    public void setPrimaryPhotoSecret(String strPrimaryPhotoSecret) {
        this.strPrimaryPhotoSecret = strPrimaryPhotoSecret;
    }

    /**
     * @return the strPhotoCount
     */
    public String getPhotoCount() {
        return strPhotoCount;
    }

    /**
     * @param strPhotoCount
     *            the strPhotoCount to set
     */
    public void setPhotoCount(String strPhotoCount) {
        this.strPhotoCount = strPhotoCount;
    }

    /**
     * @return the strVideoCount
     */
    public String getVideoCount() {
        return strVideoCount;
    }

    /**
     * @param strVideoCount
     *            the strVideoCount to set
     */
    public void setVideoCount(String strVideoCount) {
        this.strVideoCount = strVideoCount;
    }

    /**
     * @param owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return this.owner;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
