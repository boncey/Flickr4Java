
package com.flickr4java.flickr.people;

import com.flickr4java.flickr.contacts.OnlineStatus;
import com.flickr4java.flickr.util.BuddyIconable;
import com.flickr4java.flickr.util.UrlUtilities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Anthony Eden
 * @version $Id: User.java,v 1.23 2010/09/12 20:13:57 x-mago Exp $
 */
public class User implements Serializable, BuddyIconable {
    private static final long serialVersionUID = 12L;

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private String id;

    private String username;

    private String description;

    private String gender;

    private boolean ignored;

    private boolean contact;

    private boolean friend;

    private boolean family;

    private boolean admin;

    private boolean pro;

    private int iconFarm;

    private int iconServer;

    private String realName;

    private String location;

    private TimeZone timeZone;

    private Date photosFirstDate;

    private Date photosFirstDateTaken;

    private Date faveDate;

    private int photosCount;

    private OnlineStatus online;

    private String awayMessage;

    private long bandwidthMax;

    private long bandwidthUsed;

    private Boolean bandwidthUnlimited;

    private String setsCreated;

    private String setsRemaining;

    private String videosUploaded;

    private String videosRemaining;

    private String videoSizeMax;

    private long filesizeMax;

    private String mbox_sha1sum;

    private String photosurl;

    private String profileurl;

    private String mobileurl;

    private boolean revContact;

    private boolean revFriend;

    private boolean revFamily;

    private String pathAlias;

    private PhotoLimits photoLimits;

    private VideoLimits videoLimits;

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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public boolean isContact() {
        return contact;
    }

    public void setContact(boolean contact) {
        this.contact = contact;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isFamily() {
        return family;
    }

    public void setFamily(boolean family) {
        this.family = family;
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

    @Override
    public int getIconFarm() {
        return iconFarm;
    }

    @Override
    public void setIconFarm(int iconFarm) {
        this.iconFarm = iconFarm;
    }

    @Override
    public void setIconFarm(String iconFarm) {
        if (iconFarm != null) {
            setIconFarm(Integer.parseInt(iconFarm));
        }
    }

    @Override
    public int getIconServer() {
        return iconServer;
    }

    @Override
    public void setIconServer(int iconServer) {
        this.iconServer = iconServer;
    }

    @Override
    public void setIconServer(String iconServer) {
        if (iconServer != null) {
            setIconServer(Integer.parseInt(iconServer));
        }
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public TimeZone setTimeZone(TimeZone string) {
        return this.timeZone = string;
    }

    /**
     * Construct the BuddyIconUrl.
     * <p>
     * If none available, return the <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @return The BuddyIconUrl
     * @deprecated use {@link #getSecureBuddyIconUrl() }
     */
    @Override
    @Deprecated
    public String getBuddyIconUrl() {
        return UrlUtilities.createBuddyIconUrl(iconFarm, iconServer, id);
    }

    public String getLocation() {
        return location;
    }

    /**
     * Construct the BuddyIconUrl using {@code https} scheme.
     * <p>
     * If none available, return the <a href="https://www.flickr.com/images/buddyicon.jpg">default</a>, or an URL assembled from farm, iconserver and nsid.
     * 
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @return The BuddyIconUrl
     */
    public String getSecureBuddyIconUrl() {
        return UrlUtilities.createSecureBuddyIconUrl(iconFarm, iconServer, id);
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
            setPhotosFirstDate(Long.parseLong(photosFirstDate) * 1000);
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
        setFaveDate(Long.parseLong(faveDate) * 1000);
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
        if (obj == this) {
            return true;
        }
        User test = (User) obj;
        return id == null ? test.id == null : id.equals(test.id);
    }

    @Override
    public int hashCode() {
        int hash = 83;
        if (id != null) {
            hash ^= id.hashCode();
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

    /**
     * Get the user's path alias, which may appear instead of nsid in urls published by Flickr. For example feeds have urls of the form
     * .../photos/${NSID_OR_PATHALIAS}/${PHOTO_ID} & .../people/${NSID_OR_PATHALIAS}. This allows clients to look up a {@link User} given such a url. (Note that
     * <code>&lt;author&gt;</code> elements in feeds have a <code>&lt;flickr:nsid&gt;</code> child which could be used instead of the lookup this method
     * enables.)
     * 
     * @return the path alias, or null
     */
    public String getPathAlias() {
        return pathAlias;
    }

    public void setPathAlias(String pathAlias) {
        this.pathAlias = pathAlias;
    }

    /**
     * @return PhotoLimits class instance
     */
    public PhotoLimits getPhotoLimits() {
        return photoLimits;
    }

    public void setPhotoLimits(PhotoLimits photoLimits) {
        this.photoLimits = photoLimits;
    }

    /**
     * @return VideoLimits class instance
     */
    public VideoLimits getVideoLimits() {
        return videoLimits;
    }

    public void setPhotoLimits(VideoLimits videoLimits) {
        this.videoLimits = videoLimits;
    }

    public Boolean isBandwidthUnlimited() {
        return bandwidthUnlimited;
    }

    public void setIsBandwidthUnlimited(Boolean bandwidthUnlimited) {
        this.bandwidthUnlimited = bandwidthUnlimited;
    }

    public String getSetsCreated() {
        return setsCreated;
    }

    public void setSetsCreated(String setsCreated) {
        this.setsCreated = setsCreated;
    }

    public String getSetsRemaining() {
        return setsRemaining;
    }

    public void setSetsRemaining(String setsRemaining) {
        this.setsRemaining = setsRemaining;
    }

    public String getVideosUploaded() {
        return videosUploaded;
    }

    public void setVideosUploaded(String videosUploaded) {
        this.videosUploaded = videosUploaded;
    }

    public String getVideosRemaining() {
        return videosRemaining;
    }

    public void setVideosRemaining(String videosRemaining) {
        this.videosRemaining = videosRemaining;
    }

    public String getVideosSizeMax() {
        return videoSizeMax;
    }

    public void setVideoSizeMax(String videoSizeMax) {
        this.videoSizeMax = videoSizeMax;
    }

}
