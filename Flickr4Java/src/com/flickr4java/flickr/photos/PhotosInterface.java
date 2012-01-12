/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.util.StringUtilities;
import com.flickr4java.flickr.util.XMLUtilities;

/**
 * Interface for working with Flickr Photos.
 *
 * @author Anthony Eden
 * @version $Id: PhotosInterface.java,v 1.51 2010/07/20 20:11:16 x-mago Exp $
 */
public class PhotosInterface {
	private static final long serialVersionUID = 12L;

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
    public static final String METHOD_RECENTLY_UPDATED ="flickr.photos.recentlyUpdated";
    public static final String METHOD_REMOVE_TAG = "flickr.photos.removeTag";
    public static final String METHOD_SEARCH = "flickr.photos.search";
    public static final String METHOD_SET_CONTENTTYPE = "flickr.photos.setContentType";
    public static final String METHOD_SET_DATES = "flickr.photos.setDates";
    public static final String METHOD_SET_META = "flickr.photos.setMeta";
    public static final String METHOD_SET_PERMS = "flickr.photos.setPerms";
    public static final String METHOD_SET_SAFETYLEVEL = "flickr.photos.setSafetyLevel";
    public static final String METHOD_SET_TAGS = "flickr.photos.setTags";
    public static final String METHOD_GET_INTERESTINGNESS = "flickr.interestingness.getList";

    private static final ThreadLocal DATE_FORMATS = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private GeoInterface geoInterface = null;

    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    public PhotosInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the geo interface.
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
     * @param photoId The photo ID
     * @param tags The tags
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void addTags(String photoId, String[] tags) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_ADD_TAGS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("tags", StringUtilities.join(tags, " ", true)));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
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
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException 
     */
    public void delete(String photoId)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_DELETE));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        // Note: This method requires an HTTP POST request.
        Response response = transport.post(transport.getPath(), parameters);
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
     * @param photoId The photo to return information for.
     * @return a list of {@link PhotoPlace} objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public List getAllContexts(String photoId) throws IOException, SAXException, FlickrException {
        List list = new ArrayList();
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_ALL_CONTEXTS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Collection coll = response.getPayloadCollection();
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            Element el = (Element) it.next();
            String id = el.getAttribute("id");
            String title = el.getAttribute("title");
            String kind = el.getTagName();

            list.add(new PhotoPlace(kind, id, title));
        }
        return list;
    }

    /**
     * Get photos from the user's contacts.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param count The number of photos to return
     * @param justFriends Set to true to only show friends photos
     * @param singlePhoto Set to true to get a single photo
     * @param includeSelf Set to true to include self
     * @return The Collection of photos
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getContactsPhotos(int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
            throws IOException, SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_CONTACTS_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        if (count > 0) {
            parameters.add(new Parameter("count", count));
        }
        if (justFriends) {
            parameters.add(new Parameter("just_friends", "1"));
        }
        if (singlePhoto) {
            parameters.add(new Parameter("single_photo", "1"));
        }
        if (includeSelf) {
            parameters.add(new Parameter("include_self", "1"));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
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
     * @param userId The user ID
     * @param count The number of photos to return
     * @param justFriends True to include friends
     * @param singlePhoto True to get a single photo
     * @param includeSelf True to include self
     * @return A collection of Photo objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getContactsPublicPhotos(String userId, int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
      throws IOException, SAXException, FlickrException {
        return getContactsPublicPhotos(userId, Extras.MIN_EXTRAS, count, justFriends, singlePhoto, includeSelf);
    }

    public PhotoList getContactsPublicPhotos(String userId, Set extras, int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
      throws IOException, SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_CONTACTS_PUBLIC_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));

        if (count > 0) {
            parameters.add(new Parameter("count", count));
        }
        if (justFriends) {
            parameters.add(new Parameter("just_friends", "1"));
        }
        if (singlePhoto) {
            parameters.add(new Parameter("single_photo", "1"));
        }
        if (includeSelf) {
            parameters.add(new Parameter("include_self", "1"));
        }

        if (extras != null) {
            StringBuffer sb = new StringBuffer();
            Iterator it = extras.iterator();
            for (int i = 0; it.hasNext(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(it.next());
            }
            parameters.add(new Parameter(Extras.KEY_EXTRAS, sb.toString()));
        }

        Response response = transport.get(transport.getPath(), parameters);
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
     * @param photoId The photo ID
     * @return The PhotoContext
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoContext getContext(String photoId)
      throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_CONTEXT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        PhotoContext photoContext = new PhotoContext();
        Collection payload = response.getPayloadCollection();
        Iterator iter = payload.iterator();
        while (iter.hasNext()) {
            Element payloadElement = (Element) iter.next();
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
     * @param dates An array of dates, denoting the periods to return counts for.
     * They should be specified smallest first.
     * @param takenDates An array of dates, denoting the periods to return 
     * counts for. They should be specified smallest first.
     * @return A Collection of Photocount objects
     */
    public Collection getCounts(Date[] dates, Date[] takenDates)
        throws IOException, SAXException, FlickrException {
        List photocounts = new ArrayList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_COUNTS));
        parameters.add(new Parameter("api_key", apiKey));

        if (dates == null && takenDates == null) {
            throw new IllegalArgumentException("You must provide a value for either dates or takenDates");
        }

        if (dates != null) {
            List dateList = new ArrayList();
            for (int i = 0; i < dates.length; i++) {
                dateList.add(String.valueOf(dates[i].getTime() / 1000L));
            }
            parameters.add(new Parameter("dates", StringUtilities.join(dateList, ",")));
        }

        if (takenDates != null) {
            List takenDateList = new ArrayList();
            for (int i = 0; i < takenDates.length; i++) {
                takenDateList.add(String.valueOf(takenDates[i].getTime() / 1000L));
            }
            parameters.add(new Parameter("taken_dates", StringUtilities.join(takenDateList, ",")));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
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
     * @param photoId The photo ID
     * @param secret The secret
     * @return A collection of Exif objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Collection getExif(String photoId, String secret)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_EXIF));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        if (secret != null) {
            parameters.add(new Parameter("secret", secret));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List exifs = new ArrayList();
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
    public Collection getFavorites(String photoId, int perPage, int page)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();

        parameters.add(new Parameter("method", METHOD_GET_FAVORITES));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }

        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List users = new ArrayList();

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
     * @param photoId The photo Id
     * @param secret The optional secret String
     * @return The Photo
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Photo getInfo(String photoId, String secret) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        if (secret != null) {
            parameters.add(new Parameter("secret", secret));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photoElement = (Element) response.getPayload();

        return PhotoUtils.createPhoto(photoElement);
    }

    /**
     * Return a collection of Photo objects not in part of any sets.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param perPage The per page
     * @param page The page
     * @return The collection of Photo objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getNotInSet(int perPage, int page) throws IOException, SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", PhotosInterface.METHOD_GET_NOT_IN_SET));
        parameters.add(new Parameter("api_key", apiKey));

        RequestContext requestContext = RequestContext.getRequestContext();

        List extras = requestContext.getExtras();
        if (extras.size() > 0) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
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
     * @param photoId The photo id
     * @return The Permissions object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Permissions getPerms(String photoId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_PERMS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element permissionsElement = response.getPayload();
        Permissions permissions = new Permissions();
        permissions.setId(permissionsElement.getAttribute("id"));
        permissions.setPublicFlag("1".equals(permissionsElement.getAttribute("ispublic")));
        permissions.setFamilyFlag("1".equals(permissionsElement.getAttribute("isfamily")));
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
     * @param extras Set of extra-fields
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return A collection of Photo objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getRecent(Set extras, int perPage, int page) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_RECENT));
        parameters.add(new Parameter("api_key", apiKey));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Get the available sizes of a Photo.
     *
     * The calling user must have permission to view the photo.
     *
     * This method uses no authentication.
     *
     * @param photoId The photo ID
     * @return A collection of {@link Size}
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Collection getSizes(String photoId) throws IOException, SAXException, FlickrException {
        return getSizes(photoId, false);
    }

    /**
     * Get the available sizes of a Photo.
     * 
     * The boolean toggle allows to (api-)sign the call.
     * 
     * This way the calling user can retrieve sizes for <b>his own</b> private photos.
     *
     * @param photoId The photo ID
     * @param sign toggle to allow optionally signing the call (Authenticate)
     * @return A collection of {@link Size}
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Collection getSizes(String photoId, boolean sign) throws IOException, SAXException, FlickrException {
        List sizes = new ArrayList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_SIZES));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (sign) {
            parameters.add(
                new Parameter(
                    "api_sig",
                    AuthUtilities.getSignature(sharedSecret, parameters)
                )
            );
        }
        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element sizesElement = response.getPayload();
        NodeList sizeNodes = sizesElement.getElementsByTagName("size");
        for (int i = 0; i < sizeNodes.getLength(); i++) {
            Element sizeElement = (Element) sizeNodes.item(i);
            Size size = new Size();
            size.setLabel(sizeElement.getAttribute("label"));
            size.setWidth(sizeElement.getAttribute("width"));
            size.setHeight(sizeElement.getAttribute("height"));
            size.setSource(sizeElement.getAttribute("source"));
            size.setUrl(sizeElement.getAttribute("url"));
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
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getUntagged(int perPage, int page)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_UNTAGGED));
        parameters.add(new Parameter("api_key", apiKey));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }


    /**
     * Returns a list of your geo-tagged photos.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param minUploadDate Minimum upload date. Photos with an upload date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxUploadDate Maximum upload date. Photos with an upload date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param minTakenDate Minimum taken date. Photos with an taken date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxTakenDate Maximum taken date. Photos with an taken date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param privacyFilter Return photos only matching a certain privacy level. Valid values are:
     * <ul>
     * <li>1 public photos</li>
     * <li>2 private photos visible to friends</li>
     * <li>3 private photos visible to family</li>
     * <li>4 private photos visible to friends & family</li>
     * <li>5 completely private photos</li>
     * </ul>
     * Set to 0 to not specify a privacy Filter.
     *
     * @see com.flickr4java.flickr.photos.Extras
     * @param sort The order in which to sort returned photos. Deafults to date-posted-desc. The possible values are: date-posted-asc, date-posted-desc, date-taken-asc, date-taken-desc, interestingness-desc, and interestingness-asc.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return photos
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     */
    public PhotoList getWithGeoData(
        Date minUploadDate, Date maxUploadDate,
        Date minTakenDate, Date maxTakenDate,
        int privacyFilter, String sort, Set extras, int perPage, int page) 
        throws FlickrException, IOException, SAXException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_WITH_GEO_DATA));
        parameters.add(new Parameter("api_key", apiKey));

        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", minTakenDate.getTime() / 1000L));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", maxTakenDate.getTime() / 1000L));
        }
        if (privacyFilter > 0) {
            parameters.add(new Parameter("privacy_filter", privacyFilter));        	
        }
        if (sort != null) {
            parameters.add(new Parameter("sort", sort));
        }
        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }


    /**
     * Returns a list of your photos which haven't been geo-tagged.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param minUploadDate Minimum upload date. Photos with an upload date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxUploadDate Maximum upload date. Photos with an upload date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param minTakenDate Minimum taken date. Photos with an taken date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxTakenDate Maximum taken date. Photos with an taken date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param privacyFilter Return photos only matching a certain privacy level. Valid values are:
     * <ul>
     * <li>1 public photos</li>
     * <li>2 private photos visible to friends</li>
     * <li>3 private photos visible to family</li>
     * <li>4 private photos visible to friends & family</li>
     * <li>5 completely private photos</li>
     * </ul>
     * Set to 0 to not specify a privacy Filter.
     *
     * @see com.flickr4java.flickr.photos.Extras
     * @param sort The order in which to sort returned photos. Deafults to date-posted-desc. The possible values are: date-posted-asc, date-posted-desc, date-taken-asc, date-taken-desc, interestingness-desc, and interestingness-asc.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return a photo list
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     */
    public PhotoList getWithoutGeoData(Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate, int privacyFilter, String sort, Set extras, int perPage, int page) throws FlickrException, IOException, SAXException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_WITHOUT_GEO_DATA));
        parameters.add(new Parameter("api_key", apiKey));

        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", minTakenDate.getTime() / 1000L));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", maxTakenDate.getTime() / 1000L));
        }
        if (privacyFilter > 0) {
            parameters.add(new Parameter("privacy_filter", privacyFilter));        	
        }
        if (sort != null) {
            parameters.add(new Parameter("sort", sort));
        }
        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }


    /**
     * Return a list of your photos that have been recently created or which have been recently modified. 
     * Recently modified may mean that the photo's metadata (title, description, tags) may have been changed or a comment has been added (or just modified somehow :-)
     *
     * This method requires authentication with 'read' permission.
     *
     * @see com.flickr4java.flickr.photos.Extras
     * @param minDate Date indicating the date from which modifications should be compared. Must be given.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return a list of photos
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException
     */
    public PhotoList recentlyUpdated(Date minDate, Set extras, int perPage, int page) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_RECENTLY_UPDATED));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("min_date", minDate.getTime() / 1000L));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Remove a tag from a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param tagId The tag ID
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void removeTag(String tagId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_REMOVE_TAG));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("tag_id", tagId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Search for photos which match the given search parameters.
     *
     * @param params The search parameters
     * @param perPage The number of photos to show per page
     * @param page The page offset
     * @return A PhotoList
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList search(SearchParameters params, int perPage, int page)
        throws IOException, SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SEARCH));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.addAll(params.getAsParameters());

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
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
     * @param params Any search parameters
     * @param perPage Number of items per page
     * @param page The page to start on
     * @return A PhotoList
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList searchInterestingness(SearchParameters params, int perPage, int page)
        throws IOException, SAXException, FlickrException {
        PhotoList photos = new PhotoList();

        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_INTERESTINGNESS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.addAll(params.getAsParameters());

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response
                .getErrorMessage());
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
            photo.setPublicFlag("1".equals(photoElement
                .getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(photoElement
                .getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement
                .getAttribute("isfamily")));
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
     * @param photoId The photo ID
     * @param contentType The contentType to set
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setContentType(String photoId, String contentType) throws IOException,
            SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_CONTENTTYPE));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("content_type", contentType));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the dates for the specified photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param datePosted The date the photo was posted or null
     * @param dateTaken The date the photo was taken or null
     * @param dateTakenGranularity The granularity of the taken date or null
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setDates(String photoId, Date datePosted, Date dateTaken, String dateTakenGranularity)
            throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_DATES));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (datePosted != null) {
            parameters.add(new Parameter("date_posted", new Long(datePosted.getTime() / 1000)));
        }

        if (dateTaken != null) {
            parameters.add(new Parameter("date_taken", ((DateFormat)DATE_FORMATS.get()).format(dateTaken)));
        }

        if (dateTakenGranularity != null) {
            parameters.add(new Parameter("date_taken_granularity", dateTakenGranularity));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the meta data for the photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param title The new title
     * @param description The new description
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setMeta(String photoId, String title, String description)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_META));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("title", title));
        parameters.add(new Parameter("description", description));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the permissions for the photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param permissions The permissions object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setPerms(String photoId, Permissions permissions) throws IOException,
            SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_PERMS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("is_public", permissions.isPublicFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", permissions.isFriendFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_family", permissions.isFamilyFlag() ? "1" : "0"));
        parameters.add(new Parameter("perm_comment", permissions.getComment()));
        parameters.add(new Parameter("perm_addmeta", permissions.getAddmeta()));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the safety level (adultness) of a photo.<p>
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param safetyLevel The safety level of the photo or null
     * @param hidden Hidden from public searches or not or null
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_SAFE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_MODERATE
     * @see com.flickr4java.flickr.Flickr#SAFETYLEVEL_RESTRICTED
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setSafetyLevel(String photoId, String safetyLevel, Boolean hidden)
            throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_SAFETYLEVEL));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (safetyLevel != null) {
            parameters.add(new Parameter("safety_level", safetyLevel));
        }

        if (hidden != null) {
            parameters.add(new Parameter("hidden", hidden.booleanValue() ? "1" : "0"));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the tags for a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param tags The tag array
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void setTags(String photoId, String[] tags)
        throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_SET_TAGS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("tags", StringUtilities.join(tags, " ", true)));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.post(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(
                response.getErrorCode(),
                response.getErrorMessage()
            );
        }
    }

    /**
     * Get the photo for the specified ID.
     * Currently maps to the getInfo() method.
     *
     * @param id The ID
     * @return The Photo
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public Photo getPhoto(String id) throws IOException, FlickrException, SAXException {
        return getPhoto(id, null);
    }

    /**
     * Get the photo for the specified ID with the given secret.
     * Currently maps to the getInfo() method.
     *
     * @param id The ID
     * @param secret The secret
     * @return The Photo
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public Photo getPhoto(String id, String secret) throws IOException, FlickrException, SAXException {
        return getInfo(id, secret);
    }

    /**
     * Request an image from the Flickr-servers.<br>
     * Callers must close the stream upon completion.<p>
     *
     * At {@link Size} you can find constants for the available sizes.
     *
     * @param photo A photo-object
     * @param size The Size
     * @return InputStream The InputStream
     * @throws IOException
     * @throws FlickrException
     */
    public InputStream getImageAsStream(Photo photo, int size)
      throws IOException, FlickrException {
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
        } else if (size == Size.ORIGINAL) {
            urlStr = photo.getOriginalUrl();
        } else {
            throw new FlickrException("0", "Unknown Photo-size");
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (transport instanceof REST) {
            if (((REST) transport).isProxyAuth()) {
                conn.setRequestProperty(
                    "Proxy-Authorization",
                    "Basic " + ((REST) transport).getProxyCredentials()
                );
            }
        }
        conn.connect();
        return conn.getInputStream();
    }

    /**
     * Request an image from the Flickr-servers.<p>
     *
     * At {@link Size} you can find constants for the available sizes.
     *
     * @param photo A photo-object
     * @param size The size
     * @return An Image
     * @throws IOException
     * @throws FlickrException
     */
    public BufferedImage getImage(Photo photo, int size)
      throws IOException, FlickrException {
        return ImageIO.read(getImageAsStream(photo, size));
    }

    /**
     * Download of an image by URL.
     *
     * @param urlStr The URL of a Photo
     * @return BufferedImage The The Image
     * @throws IOException
     */
    public BufferedImage getImage(String urlStr)
      throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (transport instanceof REST) {
            if (((REST) transport).isProxyAuth()) {
                conn.setRequestProperty(
                    "Proxy-Authorization",
                    "Basic " + ((REST) transport).getProxyCredentials()
                );
            }
        }
        conn.connect();
        InputStream in = null;
        try {
            in = conn.getInputStream();
            return ImageIO.read(in);
        } finally {
            IOUtilities.close(in);
        }
    }
}
