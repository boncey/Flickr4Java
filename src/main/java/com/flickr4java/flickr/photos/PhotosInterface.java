
package com.flickr4java.flickr.photos;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for working with Flickr Photos.
 * 
 * @author Anthony Eden
 * @version $Id: PhotosInterface.java,v 1.51 2010/07/20 20:11:16 x-mago Exp $
 */
public class PhotosInterface {

    public static final String METHOD_ADD_TAGS = "flickr.photos.addTags";

    public static final String METHOD_DELETE = "flickr.photos.delete";

    public static final String METHOD_GET_ALL_CONTEXTS = "flickr.photos.getAllContexts";

    public static final String METHOD_GET_CONTACTS_PHOTOS = "flickr.photos.getContactsPhotos";

    public static final String METHOD_GET_CONTACTS_PUBLIC_PHOTOS = "flickr.photos.getContactsPublicPhotos";

    public static final String METHOD_GET_CONTEXT = "flickr.photos.getContext";

    public static final String METHOD_GET_COUNTS = "flickr.photos.getCounts";

    public static final String METHOD_GET_EXIF = "flickr.photos.getExif";

    public static final String METHOD_GET_FAVORITES = "flickr.photos.getFavorites";

    public static final String METHOD_GET_INFO = "flickr.photos.getInfo";

    public static final String METHOD_GET_NOT_IN_SET = "flickr.photos.getNotInSet";

    public static final String METHOD_GET_PERMS = "flickr.photos.getPerms";

    public static final String METHOD_GET_RECENT = "flickr.photos.getRecent";

    public static final String METHOD_GET_SIZES = "flickr.photos.getSizes";

    public static final String METHOD_GET_UNTAGGED = "flickr.photos.getUntagged";

    public static final String METHOD_GET_WITH_GEO_DATA = "flickr.photos.getWithGeoData";

    public static final String METHOD_GET_WITHOUT_GEO_DATA = "flickr.photos.getWithoutGeoData";

    public static final String METHOD_RECENTLY_UPDATED = "flickr.photos.recentlyUpdated";

    public static final String METHOD_REMOVE_TAG = "flickr.photos.removeTag";

    public static final String METHOD_SEARCH = "flickr.photos.search";

    public static final String METHOD_SET_CONTENTTYPE = "flickr.photos.setContentType";

    public static final String METHOD_SET_DATES = "flickr.photos.setDates";

    public static final String METHOD_SET_META = "flickr.photos.setMeta";

    public static final String METHOD_SET_PERMS = "flickr.photos.setPerms";

    public static final String METHOD_SET_SAFETYLEVEL = "flickr.photos.setSafetyLevel";

    public static final String METHOD_SET_TAGS = "flickr.photos.setTags";

    public static final String METHOD_GET_INTERESTINGNESS = "flickr.interestingness.getList";

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private GeoInterface geoInterface = null;

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transport;

    public PhotosInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the geo interface.
     * 
     * @return Access class to the flickr.photos.geo methods.
     */
    public synchronized GeoInterface getGeoInterface() {
        if (geoInterface == null) {
            geoInterface = new GeoInterface(apiKey, sharedSecret, transport);
        }
        return geoInterface;
    }

    /**
     * Add tags to a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo ID
     * @param tags
     *            The tags
     * @throws FlickrException
     */
    public void addTags(String photoId, String[] tags) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD_TAGS);

        parameters.put("photo_id", photoId);
        parameters.put("tags", StringUtilities.join(tags, " ", true));

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Delete a photo from flickr.
     * 
     * This method requires authentication with 'delete' permission.
     * 
     * @param photoId
     * @throws FlickrException
     */
    public void delete(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_DELETE);

        parameters.put("photo_id", photoId);

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty
        // sucess response if it completes without error.
    }

    /**
     * Returns all visble sets and pools the photo belongs to.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo to return information for.
     * @return a list of {@link PhotoContext} objects
     * @throws FlickrException
     */
    public PhotoAllContext getAllContexts(String photoId) throws FlickrException {
        PhotoSetList<PhotoSet> setList = new PhotoSetList<PhotoSet>();
        PoolList<Pool> poolList = new PoolList<Pool>();
        PhotoAllContext allContext = new PhotoAllContext();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_ALL_CONTEXTS);

        parameters.put("photo_id", photoId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Collection<Element> photosElement = response.getPayloadCollection();

        for (Element setElement : photosElement) {
            if (setElement.getTagName().equals("set")) {
                PhotoSet pset = new PhotoSet();
                pset.setTitle(setElement.getAttribute("title"));
                pset.setSecret(setElement.getAttribute("secret"));
                pset.setId(setElement.getAttribute("id"));
                pset.setFarm(setElement.getAttribute("farm"));
                pset.setPrimary(setElement.getAttribute("primary"));
                pset.setServer(setElement.getAttribute("server"));
                pset.setViewCount(Integer.parseInt(setElement.getAttribute("view_count")));
                pset.setCommentCount(Integer.parseInt(setElement.getAttribute("comment_count")));
                pset.setCountPhoto(Integer.parseInt(setElement.getAttribute("count_photo")));
                pset.setCountVideo(Integer.parseInt(setElement.getAttribute("count_video")));
                setList.add(pset);
                allContext.setPhotoSetList(setList);
            } else if (setElement.getTagName().equals("pool")) {
                Pool pool = new Pool();
                pool.setTitle(setElement.getAttribute("title"));
                pool.setId(setElement.getAttribute("id"));
                pool.setUrl(setElement.getAttribute("url"));
                pool.setIconServer(setElement.getAttribute("iconserver"));
                pool.setIconFarm(setElement.getAttribute("iconfarm"));
                pool.setMemberCount(Integer.parseInt(setElement.getAttribute("members")));
                pool.setPoolCount(Integer.parseInt(setElement.getAttribute("pool_count")));
                poolList.add(pool);
                allContext.setPoolList(poolList);
            }
        }

        return allContext;

    }

    /**
     * Get photos from the user's contacts.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param count
     *            The number of photos to return
     * @param justFriends
     *            Set to true to only show friends photos
     * @param singlePhoto
     *            Set to true to get a single photo
     * @param includeSelf
     *            Set to true to include self
     * @return The Collection of photos
     * @throws FlickrException
     */
    public PhotoList<Photo> getContactsPhotos(int count, boolean justFriends, boolean singlePhoto, boolean includeSelf) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTACTS_PHOTOS);

        if (count > 0) {
            parameters.put("count", Integer.toString(count));
        }
        if (justFriends) {
            parameters.put("just_friends", "1");
        }
        if (singlePhoto) {
            parameters.put("single_photo", "1");
        }
        if (includeSelf) {
            parameters.put("include_self", "1");
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        photos.setPage("1");
        photos.setPages("1");
        photos.setPerPage("" + photoNodes.getLength());
        photos.setTotal("" + photoNodes.getLength());
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Get public photos from the user's contacts.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param userId
     *            The user ID
     * @param count
     *            The number of photos to return
     * @param justFriends
     *            True to include friends
     * @param singlePhoto
     *            True to get a single photo
     * @param includeSelf
     *            True to include self
     * @return A collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getContactsPublicPhotos(String userId, int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
            throws FlickrException {
        return getContactsPublicPhotos(userId, Extras.MIN_EXTRAS, count, justFriends, singlePhoto, includeSelf);
    }

    public PhotoList<Photo> getContactsPublicPhotos(String userId, Set<String> extras, int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
            throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTACTS_PUBLIC_PHOTOS);

        parameters.put("user_id", userId);

        if (count > 0) {
            parameters.put("count", Integer.toString(count));
        }
        if (justFriends) {
            parameters.put("just_friends", "1");
        }
        if (singlePhoto) {
            parameters.put("single_photo", "1");
        }
        if (includeSelf) {
            parameters.put("include_self", "1");
        }

        if (extras != null) {
            StringBuffer sb = new StringBuffer();
            Iterator<String> it = extras.iterator();
            for (int i = 0; it.hasNext(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(it.next());
            }
            parameters.put(Extras.KEY_EXTRAS, sb.toString());
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        photos.setPage("1");
        photos.setPages("1");
        photos.setPerPage("" + photoNodes.getLength());
        photos.setTotal("" + photoNodes.getLength());
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Get the context for the specified photo.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo ID
     * @return The PhotoContext
     * @throws FlickrException
     */
    public PhotoContext getContext(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CONTEXT);

        parameters.put("photo_id", photoId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        PhotoContext photoContext = new PhotoContext();
        Collection<Element> payload = response.getPayloadCollection();
        for (Element payloadElement : payload) {
            String tagName = payloadElement.getTagName();
            if (tagName.equals("prevphoto")) {
                Photo photo = new Photo();
                photo.setId(payloadElement.getAttribute("id"));
                photo.setSecret(payloadElement.getAttribute("secret"));
                photo.setTitle(payloadElement.getAttribute("title"));
                photo.setFarm(payloadElement.getAttribute("farm"));
                photo.setUrl(payloadElement.getAttribute("url"));
                photoContext.setPreviousPhoto(photo);
            } else if (tagName.equals("nextphoto")) {
                Photo photo = new Photo();
                photo.setId(payloadElement.getAttribute("id"));
                photo.setSecret(payloadElement.getAttribute("secret"));
                photo.setTitle(payloadElement.getAttribute("title"));
                photo.setFarm(payloadElement.getAttribute("farm"));
                photo.setUrl(payloadElement.getAttribute("url"));
                photoContext.setNextPhoto(photo);
            }
        }
        return photoContext;
    }

    /**
     * Gets a collection of photo counts for the given date ranges for the calling user.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param dates
     *            An array of dates, denoting the periods to return counts for. They should be specified smallest first.
     * @param takenDates
     *            An array of dates, denoting the periods to return counts for. They should be specified smallest first.
     * @return A Collection of Photocount objects
     */
    public Collection<Photocount> getCounts(Date[] dates, Date[] takenDates) throws FlickrException {
        List<Photocount> photocounts = new ArrayList<Photocount>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_COUNTS);

        if (dates == null && takenDates == null) {
            throw new IllegalArgumentException("You must provide a value for either dates or takenDates");
        }

        if (dates != null) {
            List<String> dateList = new ArrayList<String>();
            for (int i = 0; i < dates.length; i++) {
                dateList.add(String.valueOf(dates[i].getTime() / 1000L));
            }
            parameters.put("dates", StringUtilities.join(dateList, ","));
        }

        if (takenDates != null) {
            List<String> takenDateList = new ArrayList<String>();
            for (int i = 0; i < takenDates.length; i++) {
                takenDateList.add(String.valueOf(takenDates[i].getTime() / 1000L));
            }
            parameters.put("taken_dates", StringUtilities.join(takenDateList, ","));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photocountsElement = response.getPayload();
        NodeList photocountNodes = photocountsElement.getElementsByTagName("photocount");
        for (int i = 0; i < photocountNodes.getLength(); i++) {
            Element photocountElement = (Element) photocountNodes.item(i);
            Photocount photocount = new Photocount();
            photocount.setCount(photocountElement.getAttribute("count"));
            photocount.setFromDate(photocountElement.getAttribute("fromdate"));
            photocount.setToDate(photocountElement.getAttribute("todate"));
            photocounts.add(photocount);
        }
        return photocounts;
    }

    /**
     * Get the Exif data for the photo.
     * 
     * The calling user must have permission to view the photo.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo ID
     * @param secret
     *            The secret
     * @return A collection of Exif objects
     * @throws FlickrException
     */
    public Collection<Exif> getExif(String photoId, String secret) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_EXIF);

        parameters.put("photo_id", photoId);
        if (secret != null) {
            parameters.put("secret", secret);
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<Exif> exifs = new ArrayList<Exif>();
        Element photoElement = response.getPayload();
        NodeList exifElements = photoElement.getElementsByTagName("exif");
        for (int i = 0; i < exifElements.getLength(); i++) {
            Element exifElement = (Element) exifElements.item(i);
            Exif exif = new Exif();
            exif.setTagspace(exifElement.getAttribute("tagspace"));
            exif.setTagspaceId(exifElement.getAttribute("tagspaceid"));
            exif.setTag(exifElement.getAttribute("tag"));
            exif.setLabel(exifElement.getAttribute("label"));
            exif.setRaw(XMLUtilities.getChildValue(exifElement, "raw"));
            exif.setClean(XMLUtilities.getChildValue(exifElement, "clean"));
            exifs.add(exif);
        }
        return exifs;
    }

    /**
     * Returns the list of people who have favorited a given photo.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     * @param perPage
     * @param page
     * @return List of {@link com.flickr4java.flickr.people.User}
     */
    public Collection<User> getFavorites(String photoId, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("method", METHOD_GET_FAVORITES);

        parameters.put("photo_id", photoId);

        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }

        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<User> users = new ArrayList<User>();

        Element userRoot = response.getPayload();
        NodeList userNodes = userRoot.getElementsByTagName("person");
        for (int i = 0; i < userNodes.getLength(); i++) {
            Element userElement = (Element) userNodes.item(i);
            User user = new User();
            user.setId(userElement.getAttribute("nsid"));
            user.setUsername(userElement.getAttribute("username"));
            user.setFaveDate(userElement.getAttribute("favedate"));
            users.add(user);
        }
        return users;
    }

    /**
     * Get all info for the specified photo.
     * 
     * The calling user must have permission to view the photo.
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo Id
     * @param secret
     *            The optional secret String
     * @return The Photo
     * @throws FlickrException
     */
    public Photo getInfo(String photoId, String secret) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);

        parameters.put("photo_id", photoId);
        if (secret != null) {
            parameters.put("secret", secret);
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photoElement = response.getPayload();

        return PhotoUtils.createPhoto(photoElement);
    }

    /**
     * Return a collection of Photo objects not in part of any sets.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param perPage
     *            The per page
     * @param page
     *            The page
     * @return The collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getNotInSet(int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", PhotosInterface.METHOD_GET_NOT_IN_SET);

        RequestContext requestContext = RequestContext.getRequestContext();

        List<String> extras = requestContext.getExtras();
        if (extras.size() > 0) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }

        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoElements = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoElements.getLength(); i++) {
            Element photoElement = (Element) photoElements.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

    /**
     * Get the permission information for the specified photo.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param photoId
     *            The photo id
     * @return The Permissions object
     * @throws FlickrException
     */
    public Permissions getPerms(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PERMS);

        parameters.put("photo_id", photoId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element permissionsElement = response.getPayload();
        Permissions permissions = new Permissions();
        permissions.setId(permissionsElement.getAttribute("id"));
        permissions.setPublicFlag("1".equals(permissionsElement.getAttribute("ispublic")));
        permissions.setFamilyFlag("1".equals(permissionsElement.getAttribute("isfamily")));
        permissions.setFriendFlag("1".equals(permissionsElement.getAttribute("isfriend")));
        permissions.setComment(permissionsElement.getAttribute("permcomment"));
        permissions.setAddmeta(permissionsElement.getAttribute("permaddmeta"));
        return permissions;
    }

    /**
     * Get a collection of recent photos.
     * 
     * This method does not require authentication.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param extras
     *            Set of extra-fields
     * @param perPage
     *            The number of photos per page
     * @param page
     *            The page offset
     * @return A collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getRecent(Set<String> extras, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_RECENT);

        if (extras != null && !extras.isEmpty()) {
            parameters.put(Extras.KEY_EXTRAS, StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Get the available sizes of a Photo.
     * 
     * The calling user must have permission to view the photo.
     * 
     * This method uses no authentication.
     * 
     * @param photoId
     *            The photo ID
     * @return A collection of {@link Size}
     * @throws FlickrException
     */
    public Collection<Size> getSizes(String photoId) throws FlickrException {
        return getSizes(photoId, false);
    }

    /**
     * Get the available sizes of a Photo.
     * 
     * The boolean toggle allows to (api-)sign the call.
     * 
     * This way the calling user can retrieve sizes for <b>his own</b> private photos.
     * 
     * @param photoId
     *            The photo ID
     * @param sign
     *            toggle to allow optionally signing the call (Authenticate)
     * @return A collection of {@link Size}
     * @throws FlickrException
     */
    public Collection<Size> getSizes(String photoId, boolean sign) throws FlickrException {
        SizeList<Size> sizes = new SizeList<Size>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_SIZES);

        parameters.put("photo_id", photoId);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element sizesElement = response.getPayload();
        sizes.setIsCanBlog("1".equals(sizesElement.getAttribute("canblog")));
        sizes.setIsCanDownload("1".equals(sizesElement.getAttribute("candownload")));
        sizes.setIsCanPrint("1".equals(sizesElement.getAttribute("canprint")));
        NodeList sizeNodes = sizesElement.getElementsByTagName("size");
        for (int i = 0; i < sizeNodes.getLength(); i++) {
            Element sizeElement = (Element) sizeNodes.item(i);
            Size size = new Size();
            size.setLabel(sizeElement.getAttribute("label"));
            size.setWidth(sizeElement.getAttribute("width"));
            size.setHeight(sizeElement.getAttribute("height"));
            size.setSource(sizeElement.getAttribute("source"));
            size.setUrl(sizeElement.getAttribute("url"));
            size.setMedia(sizeElement.getAttribute("media"));
            sizes.add(size);
        }
        return sizes;
    }

    /**
     * Get the collection of untagged photos.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param perPage
     * @param page
     * @return A Collection of Photos
     * @throws FlickrException
     */
    public PhotoList<Photo> getUntagged(int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_UNTAGGED);

        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Returns a list of your geo-tagged photos.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param minUploadDate
     *            Minimum upload date. Photos with an upload date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxUploadDate
     *            Maximum upload date. Photos with an upload date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param minTakenDate
     *            Minimum taken date. Photos with an taken date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxTakenDate
     *            Maximum taken date. Photos with an taken date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param privacyFilter
     *            Return photos only matching a certain privacy level. Valid values are:
     *            <ul>
     *            <li>1 public photos</li>
     *            <li>2 private photos visible to friends</li>
     *            <li>3 private photos visible to family</li>
     *            <li>4 private photos visible to friends & family</li>
     *            <li>5 completely private photos</li>
     *            </ul>
     *            Set to 0 to not specify a privacy Filter.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param sort
     *            The order in which to sort returned photos. Deafults to date-posted-desc. The possible values are: date-posted-asc, date-posted-desc,
     *            date-taken-asc, date-taken-desc, interestingness-desc, and interestingness-asc.
     * @param extras
     *            A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload,
     *            date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage
     *            Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page
     *            The page of results to return. If this argument is 0, it defaults to 1.
     * @return photos
     * @throws FlickrException
     */
    public PhotoList<Photo> getWithGeoData(Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate, int privacyFilter, String sort,
            Set<String> extras, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_WITH_GEO_DATA);

        if (minUploadDate != null) {
            parameters.put("min_upload_date", Long.toString(minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.put("max_upload_date", Long.toString(maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.put("min_taken_date", Long.toString(minTakenDate.getTime() / 1000L));
        }
        if (maxTakenDate != null) {
            parameters.put("max_taken_date", Long.toString(maxTakenDate.getTime() / 1000L));
        }
        if (privacyFilter > 0) {
            parameters.put("privacy_filter", Integer.toString(privacyFilter));
        }
        if (sort != null) {
            parameters.put("sort", sort);
        }
        if (extras != null && !extras.isEmpty()) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Returns a list of your photos which haven't been geo-tagged.
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @param minUploadDate
     *            Minimum upload date. Photos with an upload date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxUploadDate
     *            Maximum upload date. Photos with an upload date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param minTakenDate
     *            Minimum taken date. Photos with an taken date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxTakenDate
     *            Maximum taken date. Photos with an taken date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param privacyFilter
     *            Return photos only matching a certain privacy level. Valid values are:
     *            <ul>
     *            <li>1 public photos</li>
     *            <li>2 private photos visible to friends</li>
     *            <li>3 private photos visible to family</li>
     *            <li>4 private photos visible to friends & family</li>
     *            <li>5 completely private photos</li>
     *            </ul>
     *            Set to 0 to not specify a privacy Filter.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param sort
     *            The order in which to sort returned photos. Deafults to date-posted-desc. The possible values are: date-posted-asc, date-posted-desc,
     *            date-taken-asc, date-taken-desc, interestingness-desc, and interestingness-asc.
     * @param extras
     *            A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload,
     *            date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage
     *            Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page
     *            The page of results to return. If this argument is 0, it defaults to 1.
     * @return a photo list
     * @throws FlickrException
     */
    public PhotoList<Photo> getWithoutGeoData(Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate, int privacyFilter, String sort,
            Set<String> extras, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_WITHOUT_GEO_DATA);

        if (minUploadDate != null) {
            parameters.put("min_upload_date", Long.toString(minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.put("max_upload_date", Long.toString(maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.put("min_taken_date", Long.toString(minTakenDate.getTime() / 1000L));
        }
        if (maxTakenDate != null) {
            parameters.put("max_taken_date", Long.toString(maxTakenDate.getTime() / 1000L));
        }
        if (privacyFilter > 0) {
            parameters.put("privacy_filter", Long.toString(privacyFilter));
        }
        if (sort != null) {
            parameters.put("sort", sort);
        }
        if (extras != null && !extras.isEmpty()) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Return a list of your photos that have been recently created or which have been recently modified. Recently modified may mean that the photo's metadata
     * (title, description, tags) may have been changed or a comment has been added (or just modified somehow :-)
     * 
     * This method requires authentication with 'read' permission.
     * 
     * @see com.flickr4java.flickr.photos.Extras
     * @param minDate
     *            Date indicating the date from which modifications should be compared. Must be given.
     * @param extras
     *            A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload,
     *            date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage
     *            Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page
     *            The page of results to return. If this argument is 0, it defaults to 1.
     * @return a list of photos
     * @throws FlickrException
     */
    public PhotoList<Photo> recentlyUpdated(Date minDate, Set<String> extras, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_RECENTLY_UPDATED);

        parameters.put("min_date", Long.toString(minDate.getTime() / 1000L));

        if (extras != null && !extras.isEmpty()) {
            parameters.put("extras", StringUtilities.join(extras, ","));
        }
        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList<Photo> photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Remove a tag from a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param tagId
     *            The tag ID
     * @throws FlickrException
     */
    public void removeTag(String tagId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REMOVE_TAG);

        parameters.put("tag_id", tagId);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Search for photos which match the given search parameters.
     * 
     * @param params
     *            The search parameters
     * @param perPage
     *            The number of photos to show per page
     * @param page
     *            The page offset
     * @return A PhotoList
     * @throws FlickrException
     */
    public PhotoList<Photo> search(SearchParameters params, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SEARCH);

        parameters.putAll(params.getAsParameters());

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
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
     * Search for interesting photos using the Flickr Interestingness algorithm.
     * 
     * @param params
     *            Any search parameters
     * @param perPage
     *            Number of items per page
     * @param page
     *            The page to start on
     * @return A PhotoList
     * @throws FlickrException
     */
    public PhotoList<Photo> searchInterestingness(SearchParameters params, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INTERESTINGNESS);

        parameters.putAll(params.getAsParameters());

        if (perPage > 0) {
            parameters.put("per_page", Integer.toString(perPage));
        }
        if (page > 0) {
            parameters.put("page", Integer.toString(page));
        }

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
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
            Photo photo = new Photo();
            photo.setId(photoElement.getAttribute("id"));

            User owner = new User();
            owner.setId(photoElement.getAttribute("owner"));
            photo.setOwner(owner);

            photo.setSecret(photoElement.getAttribute("secret"));
            photo.setServer(photoElement.getAttribute("server"));
            photo.setFarm(photoElement.getAttribute("farm"));
            photo.setTitle(photoElement.getAttribute("title"));
            photo.setPublicFlag("1".equals(photoElement.getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(photoElement.getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement.getAttribute("isfamily")));
            photos.add(photo);
        }
        return photos;
    }

    /**
     * Set the content type of a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_PHOTO
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_SCREENSHOT
     * @see com.flickr4java.flickr.Flickr#CONTENTTYPE_OTHER
     * @param photoId
     *            The photo ID
     * @param contentType
     *            The contentType to set
     * @throws FlickrException
     */
    public void setContentType(String photoId, String contentType) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_CONTENTTYPE);

        parameters.put("photo_id", photoId);
        parameters.put("content_type", contentType);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the dates for the specified photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo ID
     * @param datePosted
     *            The date the photo was posted or null
     * @param dateTaken
     *            The date the photo was taken or null
     * @param dateTakenGranularity
     *            The granularity of the taken date or null
     * @throws FlickrException
     */
    public void setDates(String photoId, Date datePosted, Date dateTaken, String dateTakenGranularity) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_DATES);

        parameters.put("photo_id", photoId);

        if (datePosted != null) {
            parameters.put("date_posted", Long.toString(datePosted.getTime() / 1000));
        }

        if (dateTaken != null) {
            parameters.put("date_taken", ((DateFormat) DATE_FORMATS.get()).format(dateTaken));
        }

        if (dateTakenGranularity != null) {
            parameters.put("date_taken_granularity", dateTakenGranularity);
        }

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the meta data for the photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo ID
     * @param title
     *            The new title
     * @param description
     *            The new description
     * @throws FlickrException
     */
    public void setMeta(String photoId, String title, String description) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_META);

        parameters.put("photo_id", photoId);
        parameters.put("title", title);
        parameters.put("description", description);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the permissions for the photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo ID
     * @param permissions
     *            The permissions object
     * @throws FlickrException
     */
    public void setPerms(String photoId, Permissions permissions) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_PERMS);

        parameters.put("photo_id", photoId);
        parameters.put("is_public", permissions.isPublicFlag() ? "1" : "0");
        parameters.put("is_friend", permissions.isFriendFlag() ? "1" : "0");
        parameters.put("is_family", permissions.isFamilyFlag() ? "1" : "0");
        parameters.put("perm_comment", Integer.toString(permissions.getComment()));
        parameters.put("perm_addmeta", Integer.toString(permissions.getAddmeta()));

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the safety level (adultness) of a photo.
     * <p>
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo ID
     * @param safetyLevel
     *            The safety level of the photo or null
     * @param hidden
     *            Hidden from public searches or not or null
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_SAFE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_MODERATE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_RESTRICTED
     * @throws FlickrException
     */
    public void setSafetyLevel(String photoId, String safetyLevel, Boolean hidden) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_SAFETYLEVEL);

        parameters.put("photo_id", photoId);

        if (safetyLevel != null) {
            parameters.put("safety_level", safetyLevel);
        }

        if (hidden != null) {
            parameters.put("hidden", hidden.booleanValue() ? "1" : "0");
        }

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the tags for a photo.
     * 
     * This method requires authentication with 'write' permission.
     * 
     * @param photoId
     *            The photo ID
     * @param tags
     *            The tag array
     * @throws FlickrException
     */
    public void setTags(String photoId, String[] tags) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_SET_TAGS);

        parameters.put("photo_id", photoId);
        parameters.put("tags", StringUtilities.join(tags, " ", true));

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Get the photo for the specified ID. Currently maps to the getInfo() method.
     * 
     * @param id
     *            The ID
     * @return The Photo
     */
    public Photo getPhoto(String id) throws FlickrException {
        return getPhoto(id, null);
    }

    /**
     * Get the photo for the specified ID with the given secret. Currently maps to the getInfo() method.
     * 
     * @param id
     *            The ID
     * @param secret
     *            The secret
     * @return The Photo
     */
    public Photo getPhoto(String id, String secret) throws FlickrException {
        return getInfo(id, secret);
    }

    /**
     * Request an image from the Flickr-servers.<br>
     * Callers must close the stream upon completion.
     * <p>
     * 
     * At {@link Size} you can find constants for the available sizes.
     * 
     * @param photo
     *            A photo-object
     * @param size
     *            The Size
     * @return InputStream The InputStream
     * @throws FlickrException
     */
    public InputStream getImageAsStream(Photo photo, int size) throws FlickrException {
        try {
            String urlStr = "";
            if (size == Size.SQUARE) {
                urlStr = photo.getSmallSquareUrl();
            } else if (size == Size.THUMB) {
                urlStr = photo.getThumbnailUrl();
            } else if (size == Size.SMALL) {
                urlStr = photo.getSmallUrl();
            } else if (size == Size.MEDIUM) {
                urlStr = photo.getMediumUrl();
            } else if (size == Size.LARGE) {
                urlStr = photo.getLargeUrl();
            } else if (size == Size.LARGE_1600) {
                urlStr = photo.getLarge1600Url();
            } else if (size == Size.LARGE_2048) {
                urlStr = photo.getLarge2048Url();
            } else if (size == Size.ORIGINAL) {
                urlStr = photo.getOriginalUrl();
            } else if (size == Size.SQUARE_LARGE) {
                urlStr = photo.getSquareLargeUrl();
            } else if (size == Size.SMALL_320) {
                urlStr = photo.getSmall320Url();
            } else if (size == Size.MEDIUM_640) {
                urlStr = photo.getMedium640Url();
            } else if (size == Size.MEDIUM_800) {
                urlStr = photo.getMedium800Url();
            } else if (size == Size.VIDEO_ORIGINAL) {
                urlStr = photo.getVideoOriginalUrl();
            } else if (size == Size.VIDEO_PLAYER) {
                urlStr = photo.getVideoPlayerUrl();
            } else if (size == Size.SITE_MP4) {
                urlStr = photo.getSiteMP4Url();
            } else if (size == Size.MOBILE_MP4) {
                urlStr = photo.getMobileMp4Url();
            } else if (size == Size.HD_MP4) {
                urlStr = photo.getHdMp4Url();
            } else {
                throw new FlickrException("0", "Unknown Photo-size");
            }
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (transport instanceof REST) {
                if (((REST) transport).isProxyAuth()) {
                    conn.setRequestProperty("Proxy-Authorization", "Basic " + ((REST) transport).getProxyCredentials());
                }
            }
            conn.connect();
            return conn.getInputStream();
        } catch (IOException e) {
            throw new FlickrException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Request an image from the Flickr-servers.
     * <p>
     * 
     * At {@link Size} you can find constants for the available sizes.
     * 
     * @param photo
     *            A photo-object
     * @param size
     *            The size
     * @return An Image
     * @throws FlickrException
     */
    public BufferedImage getImage(Photo photo, int size) throws FlickrException {
        try {
            return ImageIO.read(getImageAsStream(photo, size));
        } catch (IOException e) {
            throw new FlickrException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Download of an image by URL.
     * 
     * @param urlStr
     *            The URL of a Photo
     * @return BufferedImage The The Image
     */
    public BufferedImage getImage(String urlStr) throws FlickrException {
        InputStream in = null;
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (transport instanceof REST) {
                if (((REST) transport).isProxyAuth()) {
                    conn.setRequestProperty("Proxy-Authorization", "Basic " + ((REST) transport).getProxyCredentials());
                }
            }
            conn.connect();
            in = conn.getInputStream();
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new FlickrException(e.getMessage(), e.getCause());
        } finally {
            IOUtilities.close(in);
        }
    }
}
