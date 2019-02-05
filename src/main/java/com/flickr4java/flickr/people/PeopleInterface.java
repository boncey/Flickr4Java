
package com.flickr4java.flickr.people;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.GroupList;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for finding Flickr users.
 * 
 * @author Anthony Eden
 * @version $Id: PeopleInterface.java,v 1.28 2010/09/12 20:13:57 x-mago Exp $
 */
public class PeopleInterface {

    public static final String METHOD_FIND_BY_EMAIL = "flickr.people.findByEmail";

    public static final String METHOD_FIND_BY_USERNAME = "flickr.people.findByUsername";

    public static final String METHOD_GET_INFO = "flickr.people.getInfo";

    public static final String METHOD_GET_ONLINE_LIST = "flickr.people.getOnlineList";

    public static final String METHOD_GET_PUBLIC_GROUPS = "flickr.people.getPublicGroups";

    public static final String METHOD_GET_PUBLIC_PHOTOS = "flickr.people.getPublicPhotos";

    public static final String METHOD_GET_UPLOAD_STATUS = "flickr.people.getUploadStatus";

    public static final String METHOD_GET_PHOTOS = "flickr.people.getPhotos";

    public static final String METHOD_GET_PHOTOS_OF = "flickr.people.getPhotosOf";

    public static final String METHOD_GET_GROUPS = "flickr.people.getGroups";

    public static final String METHOD_GET_LIMITS = "flickr.people.getLimits";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public PeopleInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Find the user by their email address.
     * 
     * This method does not require authentication.
     * 
     * @param email
     *            The email address
     * @return The User
     * @throws FlickrException
     */
    public User findByEmail(String email) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_FIND_BY_EMAIL);

        parameters.put("find_email", email);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Find a User by the username.
     * 
     * This method does not require authentication.
     * 
     * @param username
     *            The username
     * @return The User object
     * @throws FlickrException
     */
    public User findByUsername(String username) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_FIND_BY_USERNAME);

        parameters.put("username", username);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Get info about the specified user.
     * 
     * This method does not require authentication.
     * 
     * @param userId
     *            The user ID
     * @return The User object
     * @throws FlickrException
     */
    public User getInfo(String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);

        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setAdmin("1".equals(userElement.getAttribute("isadmin")));
        user.setPro("1".equals(userElement.getAttribute("ispro")));
        user.setIconFarm(userElement.getAttribute("iconfarm"));
        user.setIconServer(userElement.getAttribute("iconserver"));
        user.setRevContact("1".equals(userElement.getAttribute("revcontact")));
        user.setRevFriend("1".equals(userElement.getAttribute("revfriend")));
        user.setRevFamily("1".equals(userElement.getAttribute("revfamily")));
        String lPathAlias = userElement.getAttribute("path_alias");
        user.setPathAlias(lPathAlias == null || "".equals(lPathAlias) ? null : lPathAlias);
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        user.setDescription(XMLUtilities.getChildValue(userElement, "description"));
        user.setGender(XMLUtilities.getChildValue(userElement, "gender"));
        user.setIgnored("1".equals(XMLUtilities.getChildValue(userElement, "ignored")));
        user.setContact("1".equals(XMLUtilities.getChildValue(userElement, "contact")));
        user.setFriend("1".equals(XMLUtilities.getChildValue(userElement, "friend")));
        user.setFamily("1".equals(XMLUtilities.getChildValue(userElement, "family")));
        user.setRealName(XMLUtilities.getChildValue(userElement, "realname"));
        user.setLocation(XMLUtilities.getChildValue(userElement, "location"));
        user.setMbox_sha1sum(XMLUtilities.getChildValue(userElement, "mbox_sha1sum"));
        user.setPhotosurl(XMLUtilities.getChildValue(userElement, "photosurl"));
        user.setProfileurl(XMLUtilities.getChildValue(userElement, "profileurl"));
        user.setMobileurl(XMLUtilities.getChildValue(userElement, "mobileurl"));

        Element photosElement = XMLUtilities.getChild(userElement, "photos");
        user.setPhotosFirstDate(XMLUtilities.getChildValue(photosElement, "firstdate"));
        user.setPhotosFirstDateTaken(XMLUtilities.getChildValue(photosElement, "firstdatetaken"));
        user.setPhotosCount(XMLUtilities.getChildValue(photosElement, "count"));

        NodeList tzNodes = userElement.getElementsByTagName("timezone");
        for (int i = 0; i < tzNodes.getLength(); i++) {
            Element tzElement = (Element) tzNodes.item(i);
            TimeZone tz = new TimeZone();
            user.setTimeZone(tz);
            tz.setLabel(tzElement.getAttribute("label"));
            tz.setTimeZoneId(tzElement.getAttribute("timezone_id"));
            tz.setOffset(tzElement.getAttribute("offset"));
        }

        return user;
    }

    /**
     * Get a collection of public groups for the user.
     * 
     * The groups will contain only the members nsid, name, admin and eighteenplus. If you want the whole group-information, you have to call
     * {@link com.flickr4java.flickr.groups.GroupsInterface#getInfo(String)}.
     * 
     * This method does not require authentication.
     * 
     * @param userId
     *            The user ID
     * @return The public groups
     * @throws FlickrException
     */
    public Collection<Group> getPublicGroups(String userId) throws FlickrException {
        List<Group> groups = new ArrayList<Group>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PUBLIC_GROUPS);

        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element groupsElement = response.getPayload();
        NodeList groupNodes = groupsElement.getElementsByTagName("group");
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Element groupElement = (Element) groupNodes.item(i);
            Group group = new Group();
            group.setId(groupElement.getAttribute("nsid"));
            group.setName(groupElement.getAttribute("name"));
            group.setAdmin("1".equals(groupElement.getAttribute("admin")));
            group.setEighteenPlus(groupElement.getAttribute("eighteenplus").equals("0") ? false : true);
            groups.add(group);
        }
        return groups;
    }

    public PhotoList<Photo> getPublicPhotos(String userId, int perPage, int page) throws FlickrException {
        return getPublicPhotos(userId, Extras.MIN_EXTRAS, perPage, page);
    }

    /**
     * Get a collection of public photos for the specified user ID.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param userId
     *            The User ID
     * @param extras
     *            Set of extra-attributes to include (may be null)
     * @param perPage
     *            The number of photos per page
     * @param page
     *            The page offset
     * @return The PhotoList collection
     * @throws FlickrException
     */
    public PhotoList<Photo> getPublicPhotos(String userId, Set<String> extras, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PUBLIC_PHOTOS);

        parameters.put("user_id", userId);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }

        if (extras != null) {
            parameters.put(Extras.KEY_EXTRAS, StringUtilities.join(extras, ","));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Get upload status for the currently authenticated user.
     * 
     * Requires authentication with 'read' permission using the new authentication API.
     * 
     * @return A User object with upload status data fields filled
     * @throws FlickrException
     */
    public User getUploadStatus() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_UPLOAD_STATUS);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("id"));
        user.setPro("1".equals(userElement.getAttribute("ispro")));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));

        Element bandwidthElement = XMLUtilities.getChild(userElement, "bandwidth");
        user.setBandwidthMax(bandwidthElement.getAttribute("max"));
        user.setBandwidthUsed(bandwidthElement.getAttribute("used"));
        user.setIsBandwidthUnlimited("1".equals(bandwidthElement.getAttribute("unlimited")));

        Element filesizeElement = XMLUtilities.getChild(userElement, "filesize");
        user.setFilesizeMax(filesizeElement.getAttribute("max"));

        Element setsElement = XMLUtilities.getChild(userElement, "sets");
        user.setSetsCreated(setsElement.getAttribute("created"));
        user.setSetsRemaining(setsElement.getAttribute("remaining"));

        Element videosElement = XMLUtilities.getChild(userElement, "videos");
        user.setVideosUploaded(videosElement.getAttribute("uploaded"));
        user.setVideosRemaining(videosElement.getAttribute("remaining"));

        Element videoSizeElement = XMLUtilities.getChild(userElement, "videosize");
        user.setVideoSizeMax(videoSizeElement.getAttribute("maxbytes"));

        return user;
    }

    public PhotoList<Photo> getPhotos(String userId, String safeSearch, Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate,
            String contentType, String privacyFilter, Set<String> extras, int perPage, int page) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PHOTOS);

        parameters.put("user_id", userId);
        if (safeSearch != null) {
            parameters.put("safe_search", safeSearch);
        }
        if (minUploadDate != null) {
            parameters.put("min_upload_date", minUploadDate);
        }
        if (maxUploadDate != null) {
            parameters.put("max_upload_date", maxUploadDate);
        }
        if (minTakenDate != null) {
            parameters.put("min_taken_date", minTakenDate);
        }
        if (maxTakenDate != null) {
            parameters.put("max_taken_date", maxTakenDate);
        }
        if (contentType != null) {
            parameters.put("content_type", contentType);
        }
        if (privacyFilter != null) {
            parameters.put("privacy_filter", privacyFilter);
        }
        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }
        if (extras != null) {
            parameters.put(Extras.KEY_EXTRAS, StringUtilities.join(extras, ","));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = new PhotoList<Photo>();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    public PhotoList<Photo> getPhotosOf(String userId, String ownerId, Set<String> extras, int perPage, int page) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PHOTOS_OF);

        parameters.put("user_id", userId);
        if (ownerId != null) {
            parameters.put("owner_id", ownerId);
        }
        if (extras != null) {
            parameters.put(Extras.KEY_EXTRAS, StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = new PhotoList<Photo>();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPerPage(photosElement.getAttribute("perpage"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            Photo photo = new Photo();
            photo.setId(photoElement.getAttribute("id"));
            photo.setSecret(photoElement.getAttribute("secret"));

            User owner = new User();
            owner.setId(photoElement.getAttribute("owner"));
            photo.setOwner(owner);
            photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
            photo.setServer(photoElement.getAttribute("server"));
            photo.setTitle(photoElement.getAttribute("title"));
            photo.setPublicFlag("1".equals(photoElement.getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(photoElement.getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement.getAttribute("isfamily")));
            photos.add(photo);
        }
        return photos;
    }

    /**
     * Add the given person to the photo. Optionally, send in co-ordinates
     * 
     * @param photoId
     * @param userId
     * @param bounds
     * @throws FlickrException
     */
    public void add(String photoId, String userId, Rectangle bounds) throws FlickrException {

        // Delegating this to photos.people.PeopleInterface - Naming standard would be to use PeopleInterface but having 2 the same name can cause issues
        com.flickr4java.flickr.photos.people.PeopleInterface pi = new com.flickr4java.flickr.photos.people.PeopleInterface(apiKey, sharedSecret, transportAPI);
        pi.add(photoId, userId, bounds);
    }

    /**
     * Delete the person from the photo
     * 
     * @param photoId
     * @param userId
     * @throws FlickrException
     */
    public void delete(String photoId, String userId) throws FlickrException {

        // Delegating this to photos.people.PeopleInterface - Naming standard would be to use PeopleInterface but having 2 the same name can cause issues
        com.flickr4java.flickr.photos.people.PeopleInterface pi = new com.flickr4java.flickr.photos.people.PeopleInterface(apiKey, sharedSecret, transportAPI);
        pi.delete(photoId, userId);
    }

    /**
     * Delete the co-ordinates that the user is shown in
     * 
     * @param photoId
     * @param userId
     * @throws FlickrException
     */
    public void deleteCoords(String photoId, String userId) throws FlickrException {

        // Delegating this to photos.people.PeopleInterface - Naming standard would be to use PeopleInterface but having 2 the same name can cause issues
        com.flickr4java.flickr.photos.people.PeopleInterface pi = new com.flickr4java.flickr.photos.people.PeopleInterface(apiKey, sharedSecret, transportAPI);
        pi.deleteCoords(photoId, userId);
    }

    /**
     * Edit the co-ordinates that the user shows in
     * 
     * @param photoId
     * @param userId
     * @param bounds
     * @throws FlickrException
     */
    public void editCoords(String photoId, String userId, Rectangle bounds) throws FlickrException {

        // Delegating this to photos.people.PeopleInterface - Naming standard would be to use PeopleInterface but having 2 the same name can cause issues
        com.flickr4java.flickr.photos.people.PeopleInterface pi = new com.flickr4java.flickr.photos.people.PeopleInterface(apiKey, sharedSecret, transportAPI);
        pi.editCoords(photoId, userId, bounds);
    }

    /**
     * Get a list of people in a given photo.
     * 
     * @param photoId
     * @throws FlickrException
     */
    public PersonTagList<PersonTag> getList(String photoId) throws FlickrException {

        // Delegating this to photos.people.PeopleInterface - Naming standard would be to use PeopleInterface but having 2 the same name can cause issues
        com.flickr4java.flickr.photos.people.PeopleInterface pi = new com.flickr4java.flickr.photos.people.PeopleInterface(apiKey, sharedSecret, transportAPI);
        return pi.getList(photoId);
    }

    /**
     * 
     * @param userId
     * @throws FlickrException
     */
    public GroupList<Group> getGroups(String userId) throws FlickrException {

        GroupList<Group> groupList = new GroupList<Group>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_GROUPS);
        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element groupsElement = response.getPayload();
        NodeList groupNodes = groupsElement.getElementsByTagName("group");
        groupList.setPage(XMLUtilities.getIntAttribute(groupsElement, "page"));
        groupList.setPages(XMLUtilities.getIntAttribute(groupsElement, "pages"));
        groupList.setPerPage(XMLUtilities.getIntAttribute(groupsElement, "perpage"));
        groupList.setTotal(XMLUtilities.getIntAttribute(groupsElement, "total"));
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Element groupElement = (Element) groupNodes.item(i);
            Group group = new Group();
            group.setId(groupElement.getAttribute("nsid"));
            group.setName(groupElement.getAttribute("name"));
            group.setAdmin("1".equals(groupElement.getAttribute("admin")));
            group.setEighteenPlus("1".equals(groupElement.getAttribute("eighteenplus")));
            group.setInvitationOnly("1".equals(groupElement.getAttribute("invitation_only")));
            group.setMembers(groupElement.getAttribute("members"));
            group.setPhotoCount(groupElement.getAttribute("pool_count"));
            groupList.add(group);
        }
        return groupList;

    }

    /**
     * Get's the user's current upload limits, User object only contains user_id
     * 
     * @return Media Limits
     */

    public User getLimits() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIMITS);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        NodeList photoNodes = userElement.getElementsByTagName("photos");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element plElement = (Element) photoNodes.item(i);
            PhotoLimits pl = new PhotoLimits();
            user.setPhotoLimits(pl);
            pl.setMaxDisplay(plElement.getAttribute("maxdisplaypx"));
            pl.setMaxUpload(plElement.getAttribute("maxupload"));
        }
        NodeList videoNodes = userElement.getElementsByTagName("videos");
        for (int i = 0; i < videoNodes.getLength(); i++) {
            Element vlElement = (Element) videoNodes.item(i);
            VideoLimits vl = new VideoLimits();
            user.setPhotoLimits(vl);
            vl.setMaxDuration(vlElement.getAttribute("maxduration"));
            vl.setMaxUpload(vlElement.getAttribute("maxupload"));
        }
        return user;
    }

}
