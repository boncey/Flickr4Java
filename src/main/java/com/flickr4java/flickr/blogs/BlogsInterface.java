
package com.flickr4java.flickr.blogs;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for working with Flickr blog configurations.
 * 
 * @author Anthony Eden
 * @version $Id: BlogsInterface.java,v 1.14 2009/07/11 20:30:27 x-mago Exp $
 */
public class BlogsInterface {

    private static final String METHOD_GET_SERVICES = "flickr.blogs.getServices";

    private static final String METHOD_GET_LIST = "flickr.blogs.getList";

    private static final String METHOD_POST_PHOTO = "flickr.blogs.postPhoto";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public BlogsInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Return a list of Flickr supported blogging services.
     * 
     * This method does not require authentication.
     * 
     * @return List of Services
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Collection<Service> getServices() throws FlickrException {
        List<Service> list = new ArrayList<Service>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_SERVICES);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element servicesElement = response.getPayload();
        NodeList serviceNodes = servicesElement.getElementsByTagName("service");
        for (int i = 0; i < serviceNodes.getLength(); i++) {
            Element serviceElement = (Element) serviceNodes.item(i);
            Service srv = new Service();
            srv.setId(serviceElement.getAttribute("id"));
            srv.setName(XMLUtilities.getValue(serviceElement));
            list.add(srv);
        }
        return list;
    }

    /**
     * Post the specified photo to a blog. Note that the Photo.title and Photo.description are used for the blog entry title and body respectively.
     * 
     * @param photo
     *            The photo metadata
     * @param blogId
     *            The blog ID
     * @param blogPassword
     *            The blog password
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public void postPhoto(Photo photo, String blogId, String blogPassword) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_POST_PHOTO);

        parameters.put("blog_id", blogId);
        parameters.put("photo_id", photo.getId());
        parameters.put("title", photo.getTitle());
        parameters.put("description", photo.getDescription());
        if (blogPassword != null) {
            parameters.put("blog_password", blogPassword);
        }

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Post the specified photo to a blog.
     * 
     * @param photo
     *            The photo metadata
     * @param blogId
     *            The blog ID
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public void postPhoto(Photo photo, String blogId) throws FlickrException {
        postPhoto(photo, blogId, null);
    }

    /**
     * Get the collection of configured blogs for the calling user.
     * 
     * @return The Collection of configured blogs
     */
    public Collection<Blog> getList() throws FlickrException {
        List<Blog> blogs = new ArrayList<Blog>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element blogsElement = response.getPayload();
        NodeList blogNodes = blogsElement.getElementsByTagName("blog");
        for (int i = 0; i < blogNodes.getLength(); i++) {
            Element blogElement = (Element) blogNodes.item(i);
            Blog blog = new Blog();
            blog.setId(blogElement.getAttribute("id"));
            blog.setName(blogElement.getAttribute("name"));
            blog.setNeedPassword("1".equals(blogElement.getAttribute("needspassword")));
            blog.setUrl(blogElement.getAttribute("url"));
            blogs.add(blog);
        }
        return blogs;
    }
}
