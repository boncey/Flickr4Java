/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.people;

import com.flickr4java.flickr.contacts.OnlineStatus;
import com.flickr4java.flickr.util.BuddyIconable;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.UrlUtilities;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * @author Anthony Eden
 * @version $Id: User.java,v 1.23 2010/09/12 20:13:57 x-mago Exp $
 */
public class User implements Serializable, BuddyIconable {
    private static final long serialVersionUID = 12L;

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATS = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private String id;

    private String username;

    private boolean admin;

    private boolean pro;

    private int iconFarm;

    private int iconServer;

    private String realName;

    private String location;

    private Date photosFirstDate;

    private Date photosFirstDateTaken;

    private Date faveDate;

    private int photosCount;

    private OnlineStatus online;

    private String awayMessage;

    private long bandwidthMax;

    private long bandwidthUsed;

    private long filesizeMax;

    private String mbox_sha1sum;

    private String photosurl;

    private String profileurl;

    private String mobileurl;

    private boolean revContact;

    private boolean revFriend;

    private boolean revFamily;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isPro() {
        return pro;
    }

    public void setPro(boolean pro) {
        this.pro = pro;
    }

    public int getIconFarm() {
        return iconFarm;
    }

    public void setIconFarm(int iconFarm) {
        this.iconFarm = iconFarm;
    }

    public void setIconFarm(String iconFarm) {
        if (iconFarm != null)
            setIconFarm(Integer.parseInt(iconFarm));
    }

    public int getIconServer() {
        return iconServer;
    }

    public void setIconServer(int iconServer) {
        this.iconServer = iconServer;
    }

    public void setIconServer(String iconServer) {
        if (iconServer != null)
            setIconServer(Integer.parseInt(iconServer));
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Construct the BuddyIconUrl.
     * <p>
     * If none available, return the <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @return The BuddyIconUrl
     */
    public String getBuddyIconUrl() {
        return UrlUtilities.createBuddyIconUrl(iconFarm, iconServer, id);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getPhotosFirstDate() {
        return photosFirstDate;
    }

    public void setPhotosFirstDate(Date photosFirstDate) {
        this.photosFirstDate = photosFirstDate;
    }

    public void setPhotosFirstDate(long photosFirstDate) {
        setPhotosFirstDate(new Date(photosFirstDate));
    }

    public void setPhotosFirstDate(String photosFirstDate) {
        if (photosFirstDate != null) {
            setPhotosFirstDate(Long.parseLong(photosFirstDate) * (long) 1000);
        }
    }

    public Date getPhotosFirstDateTaken() {
        return photosFirstDateTaken;
    }

    public void setPhotosFirstDateTaken(Date photosFirstDateTaken) {
        this.photosFirstDateTaken = photosFirstDateTaken;
    }

    public void setPhotosFirstDateTaken(String photosFirstDateTaken) {
        if (photosFirstDateTaken != null) {
            try {
                setPhotosFirstDateTaken(((DateFormat) DATE_FORMATS.get()).parse(photosFirstDateTaken));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setFaveDate(String faveDate) {
        setFaveDate(Long.parseLong(faveDate) * (long) 1000);
    }

    public void setFaveDate(long faveDate) {
        setFaveDate(new Date(faveDate));
    }

    /**
     * Date when User has faved a Photo.<br>
     * flickr.photos.getFavorites returns person-data where this Date will be set.
     * 
     * @param faveDate
     */
    public void setFaveDate(Date faveDate) {
        this.faveDate = faveDate;
    }

    /**
     * The Date, when a User has favourited a Photo.<br>
     * This value is set, when a User is created by {@link com.flickr4java.flickr.photos.PhotosInterface#getFavorites(String, int, int)}.
     * 
     * @return faveDate
     */
    public Date getFaveDate() {
        return faveDate;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }

    public void setPhotosCount(String photosCount) {
        if (photosCount != null) {
            setPhotosCount(Integer.parseInt(photosCount));
        }
    }

    public OnlineStatus getOnline() {
        return online;
    }

    public void setOnline(OnlineStatus online) {
        this.online = online;
    }

    public String getAwayMessage() {
        return awayMessage;
    }

    public void setAwayMessage(String awayMessage) {
        this.awayMessage = awayMessage;
    }

    public long getBandwidthMax() {
        return bandwidthMax;
    }

    public void setBandwidthMax(long bandwidthMax) {
        this.bandwidthMax = bandwidthMax;
    }

    public void setBandwidthMax(String bandwidthMax) {
        if (bandwidthMax != null) {
            setBandwidthMax(Long.parseLong(bandwidthMax));
        }
    }

    public long getBandwidthUsed() {
        return bandwidthUsed;
    }

    public void setBandwidthUsed(long bandwidthUsed) {
        this.bandwidthUsed = bandwidthUsed;
    }

    public void setBandwidthUsed(String bandwidthUsed) {
        if (bandwidthUsed != null) {
            setBandwidthUsed(Long.parseLong(bandwidthUsed));
        }
    }

    public long getFilesizeMax() {
        return filesizeMax;
    }

    public void setFilesizeMax(long filesizeMax) {
        this.filesizeMax = filesizeMax;
    }

    public void setFilesizeMax(String filesizeMax) {
        if (filesizeMax != null) {
            setFilesizeMax(Long.parseLong(filesizeMax));
        }
    }

    public void setMbox_sha1sum(String mbox_sha1sum) {
        this.mbox_sha1sum = mbox_sha1sum;
    }

    public String getMbox_sha1sum() {
        return this.mbox_sha1sum;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        // object must be User at this point
        User test = (User) obj;
        Class cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                try {
                    Object res = method[i].invoke(this);
                    Object resTest = method[i].invoke(test);
                    String retType = method[i].getReturnType().toString();
                    if (retType.indexOf("class") == 0) {
                        if (res != null && resTest != null) {
                            if (!res.equals(resTest))
                                return false;
                        } else {
                            if (res == null && resTest == null) {
                                // nop
                            } else if (res == null || resTest == null) {
                                // one ist set and one is null
                                return false;
                            }
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer) resTest)))
                            return false;
                    } else if (retType.equals("boolean")) {
                        if (!((Boolean) res).equals(((Boolean) resTest)))
                            return false;
                    } else if (retType.equals("long")) {
                        if (!((Long) res).equals(((Long) resTest)))
                            return false;
                    } else {
                        System.out.println("User#equals() missing type " + method[i].getName() + "|" + method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    // System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 1;
        Class cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                Object res = null;
                try {
                    res = method[i].invoke(this);
                } catch (IllegalAccessException ex) {
                    System.out.println("hashCode " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    // System.out.println("hashCode " + method[i].getName() + " " + ex);
                }
                if (res != null) {
                    if (res instanceof Boolean) {
                        Boolean bool = (Boolean) res;
                        hash += bool.hashCode();
                    } else if (res instanceof Integer) {
                        Integer inte = (Integer) res;
                        hash += inte.hashCode();
                    } else if (res instanceof String) {
                        String str = (String) res;
                        hash += str.hashCode();
                    } else if (res instanceof Long) {
                        Long lon = (Long) res;
                        hash += lon.hashCode();
                    } else if (res instanceof OnlineStatus) {
                        OnlineStatus os = (OnlineStatus) res;
                        hash += os.hashCode();
                    } else {
                        System.out.println("User hashCode unrecognised object: " + res.getClass().getName());
                    }
                }
            }
        }
        return hash;
    }

    /**
     * @return the photosurl
     */
    public String getPhotosurl() {
        return photosurl;
    }

    /**
     * @param photosurl
     *            the photosurl to set
     */
    public void setPhotosurl(String photosurl) {
        this.photosurl = photosurl;
    }

    /**
     * @return the profileurl
     */
    public String getProfileurl() {
        return profileurl;
    }

    /**
     * @param profileurl
     *            the profileurl to set
     */
    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    /**
     * @return the mobileurl
     */
    public String getMobileurl() {
        return mobileurl;
    }

    /**
     * @param mobileurl
     *            the mobileurl to set
     */
    public void setMobileurl(String mobileurl) {
        this.mobileurl = mobileurl;
    }

    public void setRevContact(boolean revContact) {
        this.revContact = revContact;
    }

    public boolean isRevContact() {
        return revContact;
    }

    public void setRevFriend(boolean revFriend) {
        this.revFriend = revFriend;
    }

    public boolean isRevFriend() {
        return revFriend;
    }

    public void setRevFamily(boolean revFamily) {
        this.revFamily = revFamily;
    }

    public boolean isRevFamily() {
        return revFamily;
    }
}
