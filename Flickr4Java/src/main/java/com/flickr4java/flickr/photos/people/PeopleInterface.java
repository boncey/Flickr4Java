/**
 * @author acaplan
 */
package com.flickr4java.flickr.photos.people;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.people.UserList;

/**
 * @author acaplan
 * 
 */
public class PeopleInterface {

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

    public PeopleInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    public static final String METHOD_ADD = "flickr.photos.people.add";

    public static final String METHOD_DELETE = "flickr.photos.people.delete";

    public static final String METHOD_DELETE_COORDS = "flickr.photos.people.deleteCoords";

    public static final String METHOD_EDIT_COORDS = "flickr.photos.people.editCoords";

    public static final String METHOD_GET_LIST = "flickr.photos.people.getList";

    /**
     * Add the given person to the photo. Optionally, send in co-ordinates
     * 
     * @param photoId
     * @param userId
     * @param personX
     * @param personY
     * @param personW
     * @param personH
     * @throws FlickrException
     */
    public void add(String photoId, String userId, Rectangle bounds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("user_id", userId);
        if (bounds != null) {
            parameters.put("person_x", bounds.x);
            parameters.put("person_y", bounds.y);
            parameters.put("person_w", bounds.width);
            parameters.put("person_h", bounds.height);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Delete the person from the photo
     * 
     * @param photoId
     * @param userId
     * @throws FlickrException
     */
    public void delete(String photoId, String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_DELETE);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Delete the co-ordinates that the user is shown in
     * 
     * @param photoId
     * @param userId
     * @throws FlickrException
     */
    public void deleteCoords(String photoId, String userId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_DELETE_COORDS);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("user_id", userId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Edit the co-ordinates that the user shows in
     * 
     * @param photoId
     * @param userId
     * @param personX
     * @param personY
     * @param personW
     * @param personH
     * @throws FlickrException
     */
    public void editCoords(String photoId, String userId, Rectangle bounds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_COORDS);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);
        parameters.put("user_id", userId);
        parameters.put("person_x", bounds.x);
        parameters.put("person_y", bounds.y);
        parameters.put("person_w", bounds.width);
        parameters.put("person_h", bounds.height);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public UserList<User> getList(String photoId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);
        parameters.put(Flickr.API_KEY, apiKey);

        parameters.put("photo_id", photoId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        // @TODO this isn't a complete list ...
        Element usersElement = response.getPayload();
        UserList<User> ul = new UserList<User>();
        NodeList usernodes = usersElement.getElementsByTagName("person");
        for (int i = 0; i < usernodes.getLength(); i++) {
            Element userElement = (Element) usernodes.item(i);

            User user = new User();
            user.setId(userElement.getAttribute("nsid"));
            user.setUsername(userElement.getAttribute("username"));
            user.setIconFarm(userElement.getAttribute("iconfarm"));
            user.setIconServer(userElement.getAttribute("iconserver"));
            user.setRealName(userElement.getAttribute("realname"));
            ul.add(user);
        }
        return ul;

    }
}
