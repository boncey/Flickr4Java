
package com.flickr4java.flickr.tags;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotoUtils;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

/**
 * Interface for working with Flickr tags.
 * 
 * @author Anthony Eden
 * @version $Id: TagsInterface.java,v 1.19 2009/07/02 21:52:35 x-mago Exp $
 */
public class TagsInterface {

    public static final String METHOD_GET_CLUSTERS = "flickr.tags.getClusters";

    public static final String METHOD_GET_HOT_LIST = "flickr.tags.getHotList";

    public static final String METHOD_GET_LIST_PHOTO = "flickr.tags.getListPhoto";

    public static final String METHOD_GET_LIST_USER = "flickr.tags.getListUser";

    public static final String METHOD_GET_LIST_USER_POPULAR = "flickr.tags.getListUserPopular";

    public static final String METHOD_GET_LIST_USER_RAW = "flickr.tags.getListUserRaw";

    public static final String METHOD_GET_RELATED = "flickr.tags.getRelated";

    public static final String METHOD_GET_CLUSTER_PHOTOS = "flickr.tags.getClusterPhotos";

    public static final String PERIOD_WEEK = "week";

    public static final String PERIOD_DAY = "day";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    /**
     * Construct a TagsInterface.
     * 
     * @param apiKey
     *            The API key
     * @param transportAPI
     *            The Transport interface
     */
    public TagsInterface(String apiKey, String sharedSecret, Transport transportAPI) {

        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Search for tag-clusters.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @since 1.2
     * @param searchTag
     * @return a list of clusters
     */
    public ClusterList getClusters(String searchTag) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CLUSTERS);

        parameters.put("tag", searchTag);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        ClusterList clusters = new ClusterList();
        Element clustersElement = response.getPayload();
        NodeList clusterElements = clustersElement.getElementsByTagName("cluster");
        for (int i = 0; i < clusterElements.getLength(); i++) {
            Cluster cluster = new Cluster();
            NodeList tagElements = ((Element) clusterElements.item(i)).getElementsByTagName("tag");
            for (int j = 0; j < tagElements.getLength(); j++) {
                Tag tag = new Tag();
                tag.setValue(((Text) tagElements.item(j).getFirstChild()).getData());
                cluster.addTag(tag);
            }
            clusters.addCluster(cluster);
        }
        return clusters;
    }

    /**
     * Returns the first 24 photos for a given tag cluster.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param tag
     * @param clusterId
     * @return PhotoList
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public PhotoList<Photo> getClusterPhotos(String tag, String clusterId) throws FlickrException {

        PhotoList<Photo> photos = new PhotoList<Photo>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_CLUSTER_PHOTOS);

        parameters.put("tag", tag);
        parameters.put("cluster_id", clusterId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
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
     * Returns a list of hot tags for the given period.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param period
     *            valid values are 'day' or 'week'
     * @param count
     *            maximum is 200
     * @return The collection of HotlistTag objects
     */
    public Collection<HotlistTag> getHotList(String period, int count) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_HOT_LIST);

        parameters.put("period", period);
        parameters.put("count", "" + count);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Collection<Element> payloadCollection = response.getPayloadCollection();
        Optional<Element> element = payloadCollection.stream().filter(payload -> payload.getTagName().equals("hottags")).findFirst();

        List<HotlistTag> tags = new ArrayList<>();
        if (element.isPresent()) {
            NodeList tagElements = element.get().getElementsByTagName("tag");
            for (int i = 0; i < tagElements.getLength(); i++) {
                Element tagElement = (Element) tagElements.item(i);
                HotlistTag tag = new HotlistTag();
                tag.setValue(((Text) tagElement.getFirstChild()).getData());
                tags.add(tag);
            }
        }
        return tags;
    }

    /**
     * Get a list of tags for the specified photo.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param photoId
     *            The photo ID
     * @return The collection of Tag objects
     */
    public Photo getListPhoto(String photoId) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST_PHOTO);

        parameters.put("photo_id", photoId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element photoElement = response.getPayload();
        Photo photo = new Photo();
        photo.setId(photoElement.getAttribute("id"));

        List<Tag> tags = new ArrayList<Tag>();
        Element tagsElement = (Element) photoElement.getElementsByTagName("tags").item(0);
        NodeList tagElements = tagsElement.getElementsByTagName("tag");
        for (int i = 0; i < tagElements.getLength(); i++) {
            Element tagElement = (Element) tagElements.item(i);
            Tag tag = new Tag();
            tag.setId(tagElement.getAttribute("id"));
            tag.setAuthor(tagElement.getAttribute("author"));
            tag.setAuthorName(tagElement.getAttribute("authorname"));
            tag.setRaw(tagElement.getAttribute("raw"));
            tag.setValue(((Text) tagElement.getFirstChild()).getData());
            tags.add(tag);
        }
        photo.setTags(tags);
        return photo;
    }

    /**
     * Get a collection of tags used by the specified user.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param userId
     *            The User ID
     * @return The User object
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Collection<Tag> getListUser(String userId) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST_USER);

        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element whoElement = response.getPayload();

        List<Tag> tags = new ArrayList<Tag>();
        Element tagsElement = (Element) whoElement.getElementsByTagName("tags").item(0);
        NodeList tagElements = tagsElement.getElementsByTagName("tag");
        for (int i = 0; i < tagElements.getLength(); i++) {
            Element tagElement = (Element) tagElements.item(i);
            Tag tag = new Tag();
            tag.setValue(((Text) tagElement.getFirstChild()).getData());
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get a list of the user's popular tags.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param userId
     *            The user ID
     * @return The collection of Tag objects
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Collection<Tag> getListUserPopular(String userId) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST_USER_POPULAR);

        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element whoElement = response.getPayload();

        List<Tag> tags = new ArrayList<Tag>();
        Element tagsElement = (Element) whoElement.getElementsByTagName("tags").item(0);
        NodeList tagElements = tagsElement.getElementsByTagName("tag");
        for (int i = 0; i < tagElements.getLength(); i++) {
            Element tagElement = (Element) tagElements.item(i);
            Tag tag = new Tag();
            tag.setCount(tagElement.getAttribute("count"));
            tag.setValue(((Text) tagElement.getFirstChild()).getData());
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get a list of the user's (identified by token) popular tags.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @return The collection of Tag objects
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Collection<TagRaw> getListUserRaw() throws FlickrException {

        return getListUserRaw(null);
    }

    /**
     * Get a list of the user's (identified by token) popular tags.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param tagVal
     *            a tag to search for (optional)
     * 
     * @return The collection of Tag objects
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Collection<TagRaw> getListUserRaw(String tagVal) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST_USER_RAW);

        if (tagVal != null) {
            parameters.put("tag", tagVal);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element whoElement = response.getPayload();

        List<TagRaw> tags = new ArrayList<TagRaw>();
        Element tagsElement = (Element) whoElement.getElementsByTagName("tags").item(0);
        NodeList tagElements = tagsElement.getElementsByTagName("tag");
        for (int i = 0; i < tagElements.getLength(); i++) {
            Element tagElement = (Element) tagElements.item(i);
            TagRaw tag = new TagRaw();
            tag.setClean(tagElement.getAttribute("clean"));
            NodeList rawElements = tagElement.getElementsByTagName("raw");
            for (int j = 0; j < rawElements.getLength(); j++) {
                Element rawElement = (Element) rawElements.item(j);
                tag.addRaw(((Text) rawElement.getFirstChild()).getData());
            }
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get the related tags.
     * 
     * <p>
     * This method does not require authentication.
     * </p>
     * 
     * @param tag
     *            The source tag
     * @return A RelatedTagsList object
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public RelatedTagsList getRelated(String tag) throws FlickrException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_RELATED);

        parameters.put("tag", tag);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element tagsElement = response.getPayload();

        RelatedTagsList tags = new RelatedTagsList();
        tags.setSource(tagsElement.getAttribute("source"));
        NodeList tagElements = tagsElement.getElementsByTagName("tag");
        for (int i = 0; i < tagElements.getLength(); i++) {
            Element tagElement = (Element) tagElements.item(i);
            Tag t = new Tag();
            t.setValue(XMLUtilities.getValue(tagElement));
            tags.add(t);
        }
        return tags;
    }

}
