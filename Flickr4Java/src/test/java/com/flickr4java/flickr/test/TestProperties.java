package com.flickr4java.flickr.test;

import com.flickr4java.flickr.FlickrRuntimeException;
import com.flickr4java.flickr.util.IOUtilities;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Wrapper for test properties.
 * 
 * @author Darren Greaves Copyright (c) 2012 Darren Greaves.
 */
public class TestProperties {

    /**
     * Logger for log4j.
     */
    private static Logger _log = Logger.getLogger(TestProperties.class);

    private String host;

    private String apiKey;

    private String secret;

    private String token;

    private String tokenSecret;

    private String nsid;

    private String email;

    private String username;

    private String displayname;

    private String groupId;

    private String testGroupId;

    private String photoId;

    private String photosetId;

    private String collectionId;

    private String collectionUrlId;

    private String imageFile;

    private String geoWritePhotoId;

    private String galleryId;

    private final List<String> photosetPhotos;

    public TestProperties() {

        photosetPhotos = new ArrayList<String>();
        Properties properties = load();
        populate(properties);
    }

    /**
     * 
     * @param properties
     */
    private void populate(Properties properties) {
        host = properties.getProperty("host");
        apiKey = properties.getProperty("apiKey");
        secret = properties.getProperty("secret");
        token = properties.getProperty("token");
        tokenSecret = properties.getProperty("tokensecret");
        nsid = properties.getProperty("nsid");
        email = properties.getProperty("email");
        displayname = properties.getProperty("displayname");
        username = properties.getProperty("username");
        groupId = properties.getProperty("groupid");
        testGroupId = properties.getProperty("testgroupid");
        photoId = properties.getProperty("photoid");
        photosetId = properties.getProperty("photosetid");
        collectionId = properties.getProperty("collectionid");
        collectionUrlId = properties.getProperty("collectionurlid");
        imageFile = properties.getProperty("imagefile");
        galleryId = properties.getProperty("galleryid");
        geoWritePhotoId = properties.getProperty("geo.write.photoid");

        String photosetPhotosCSV = properties.getProperty("photosetphotos");
        String[] photosetPhotos = photosetPhotosCSV != null ? photosetPhotosCSV.split(",") : new String[0];
        for (String photosetPhoto : photosetPhotos) {
            this.photosetPhotos.add(photosetPhoto.trim());
        }
        Collections.sort(this.photosetPhotos);
    }

    private Properties load() {
        Properties properties = new Properties();
        InputStream in = null;
        String setupPropertiesPath = System.getenv("SETUP_PROPERTIES_PATH");

        try {
            if (setupPropertiesPath != null) {
                _log.info("Using properties file at " + setupPropertiesPath);
                in = new FileInputStream(new File(setupPropertiesPath));
            } else {
                in = getClass().getResourceAsStream("/setup.properties");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new FlickrRuntimeException("Problem loading setup.properties", e);
        } finally {
            IOUtilities.close(in);
        }

        return properties;
    }

    public String getHost() {
        return host;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getNsid() {
        return nsid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTestGroupId() {
        return testGroupId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getPhotosetId() {
        return photosetId;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getGeoWritePhotoId() {
        return geoWritePhotoId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getCollectionUrlId() {
        return collectionUrlId;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public String getDisplayname() {
        return displayname;
    }

    public List<String> getPhotosetPhotos() {
        return photosetPhotos;
    }

}
