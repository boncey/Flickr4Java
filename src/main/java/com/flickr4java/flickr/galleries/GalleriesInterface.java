/**
 * @author acaplan
 */
package com.flickr4java.flickr.galleries;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author acaplan
 * 
 */
public class GalleriesInterface {

    public static final String METHOD_ADD_PHOTO = "flickr.galleries.addPhoto";

    public static final String METHOD_CREATE = "flickr.galleries.create";

    public static final String METHOD_EDIT_META = "flickr.galleries.editMeta";

    public static final String METHOD_EDIT_PHOTO = "flickr.galleries.editPhoto";

    public static final String METHOD_EDIT_PHOTOS = "flickr.galleries.editPhotos";

    public static final String METHOD_GET_INFO = "flickr.galleries.getInfo";

    public static final String METHOD_GET_LIST = "flickr.galleries.getList";

    public static final String METHOD_GET_LIST_FOR_PHOTO = "flickr.galleries.getListForPhoto";

    public static final String METHOD_GET_PHOTOS = "flickr.galleries.getPhotos";

    private String apiKey;

    private String sharedSecret;

    private Transport transport;

    /**
     * Construct a GalleriesInterface.
     * 
     * @param apiKey
     *            The API key
     * @param transportAPI
     *            The Transport interface
     */
    public GalleriesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transportAPI;
    }

    /**
     * Return the list of galleries created by a user. Sorted from newest to oldest.
     * 
     * @param userId
     *            The user you want to check for
     * @param perPage
     *            Number of galleries per page
     * @param page
     *            The page number
     * @return gallery
     * @throws FlickrException
     * 
     * @see <a href="https://www.flickr.com/services/api/flickr.galleries.getList.html">flickr.galleries.getList</a>
     */
    public List<Gallery> getList(String userId, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);
        parameters.put("user_id", userId);
        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element element = response.getPayload();
        GalleryList<Gallery> galleries = new GalleryList<Gallery>();
        galleries.setPage(element.getAttribute("page"));
        galleries.setPages(element.getAttribute("pages"));
        galleries.setPerPage(element.getAttribute("per_page"));
        galleries.setTotal(element.getAttribute("total"));

        NodeList galleryNodes = element.getElementsByTagName("gallery");
        for (int i = 0; i < galleryNodes.getLength(); i++) {
            Element galleryElement = (Element) galleryNodes.item(i);
            Gallery gallery = new Gallery();
            gallery.setId(galleryElement.getAttribute("id"));
            gallery.setUrl(galleryElement.getAttribute("url"));

            User owner = new User();
            owner.setId(galleryElement.getAttribute("owner"));
            gallery.setOwner(owner);
            gallery.setCreateDate(galleryElement.getAttribute("date_create"));
            gallery.setUpdateDate(galleryElement.getAttribute("date_update"));
            gallery.setPrimaryPhotoId(galleryElement.getAttribute("primary_photo_id"));
            gallery.setPrimaryPhotoServer(galleryElement.getAttribute("primary_photo_server"));
            gallery.setPrimaryPhotoFarm(galleryElement.getAttribute("primary_photo_farm"));
            gallery.setPrimaryPhotoSecret(galleryElement.getAttribute("primary_photo_secret"));
            gallery.setPhotoCount(galleryElement.getAttribute("count_photos"));
            gallery.setVideoCount(galleryElement.getAttribute("count_videos"));

            galleries.add(gallery);
        }
        return galleries;
    }

    public void addPhoto(String strGalleryId, String photoId, String strComment) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD_PHOTO);
        parameters.put("gallery_id", strGalleryId);
        parameters.put("photo_id", photoId);
        parameters.put("comment", strComment);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void editMeta(String strGalleryId, String strTitle, String strDescription) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_META);
        parameters.put("title", strTitle);
        parameters.put("description", strDescription);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void editPhoto(String strGalleryId, String strPhotoId, String strComment) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_PHOTO);
        parameters.put("gallery_id", strGalleryId);
        parameters.put("photo_id", strPhotoId);
        parameters.put("comment", strComment);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void editPhotos(String strGalleryId, String strPrimaryPhotoId, String strPhotoIds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_PHOTOS);
        parameters.put("gallery_id", strGalleryId);
        parameters.put("primary_photo_id", strPrimaryPhotoId);
        parameters.put("photo_ids", strPhotoIds);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public Gallery getInfo(String strGalleryId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);
        parameters.put("gallery_id", strGalleryId);

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element galleryElement = response.getPayload();
        Gallery gallery = new Gallery();
        gallery.setId(galleryElement.getAttribute("id"));
        gallery.setUrl(galleryElement.getAttribute("url"));

        User owner = new User();
        owner.setId(galleryElement.getAttribute("owner"));
        gallery.setOwner(owner);
        gallery.setCreateDate(galleryElement.getAttribute("date_create"));
        gallery.setUpdateDate(galleryElement.getAttribute("date_update"));
        gallery.setPrimaryPhotoId(galleryElement.getAttribute("primary_photo_id"));
        gallery.setPrimaryPhotoServer(galleryElement.getAttribute("primary_photo_server"));
        gallery.setPrimaryPhotoFarm(galleryElement.getAttribute("primary_photo_farm"));
        gallery.setPrimaryPhotoSecret(galleryElement.getAttribute("primary_photo_secret"));
        gallery.setPhotoCount(galleryElement.getAttribute("count_photos"));
        gallery.setVideoCount(galleryElement.getAttribute("count_videos"));

        gallery.setTitle(XMLUtilities.getChildValue(galleryElement, "title"));
        gallery.setDesc(XMLUtilities.getChildValue(galleryElement, "description"));
        return gallery;
    }

    /**
     * 
     * @param strTitle
     * @param strDescription
     * @param primaryPhotoId
     * @throws FlickrException
     */
    public Gallery create(String strTitle, String strDescription, String primaryPhotoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_CREATE);
        parameters.put("title", strTitle);
        parameters.put("description", strDescription);
        if (primaryPhotoId != null) {
            parameters.put("primary_photo_id ", primaryPhotoId);
        }

        Response response = transport.post(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element element = response.getPayload();
        NodeList galleryNodes = element.getElementsByTagName("gallery");
        Element galleryElement = (Element) galleryNodes.item(0);
        Gallery gallery = new Gallery();
        gallery.setId(galleryElement.getAttribute("id"));
        gallery.setUrl(galleryElement.getAttribute("url"));
        gallery.setTitle(strTitle);
        gallery.setDesc(strDescription);
        return gallery;
    }

    /**
     * Get the photos for the specified gallery
     * 
     * This method does not require authentication.
     * 
     * @param galleryId
     *            The group ID
     * @param extras
     *            Set of extra-attributes to include (may be null)
     * @param perPage
     *            The number of photos per page (0 to ignore)
     * @param page
     *            The page offset (0 to ignore)
     * @return A Collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getPhotos(String galleryId, Set<String> extras, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_PHOTOS);

        parameters.put("gallery_id", galleryId);

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

        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
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
            photo.setSecret(photoElement.getAttribute("secret"));

            User owner = new User();
            owner.setId(photoElement.getAttribute("owner"));
            photo.setOwner(owner);
            photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
            photo.setServer(photoElement.getAttribute("server"));
            photo.setFarm(photoElement.getAttribute("farm"));
            photo.setTitle(photoElement.getAttribute("title"));
            photo.setPublicFlag("1".equals(photoElement.getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(photoElement.getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement.getAttribute("isfamily")));
            photo.setPrimary("1".equals(photoElement.getAttribute("is_primary")));
            photo.setComments(photoElement.getAttribute("has_comment"));
            photos.add(photo);
        }

        return photos;
    }

    /**
     * 
     * This method does not require authentication.
     * 
     * @param photoId
     *            The photo ID
     * @param perPage
     *            The number of photos per page (0 to ignore)
     * @param page
     *            The page offset (0 to ignore)
     * @return A Collection of Photo objects
     * @throws FlickrException
     */
    public PhotoList<Photo> getListForPhoto(String photoId, int perPage, int page) throws FlickrException {
        PhotoList<Photo> photos = new PhotoList<Photo>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST_FOR_PHOTO);

        parameters.put("photo_id", photoId);

        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
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
            photo.setSecret(photoElement.getAttribute("secret"));

            User owner = new User();
            owner.setId(photoElement.getAttribute("owner"));
            photo.setOwner(owner);
            photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
            photo.setServer(photoElement.getAttribute("server"));
            photo.setFarm(photoElement.getAttribute("farm"));
            photo.setTitle(photoElement.getAttribute("title"));
            photo.setPublicFlag("1".equals(photoElement.getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(photoElement.getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement.getAttribute("isfamily")));
            photo.setPrimary("1".equals(photoElement.getAttribute("is_primary")));
            photo.setComments(photoElement.getAttribute("has_comment"));
            photos.add(photo);
        }

        return photos;
    }
}
