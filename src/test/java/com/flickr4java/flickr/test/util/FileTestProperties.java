package com.flickr4java.flickr.test.util;

import com.flickr4java.flickr.FlickrRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class FileTestProperties implements TestProperties {

    /**
     * Logger for log4j.
     */
    private static Logger _log = LoggerFactory.getLogger(FileTestProperties.class);

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

    public FileTestProperties(File propertiesFile) {
        photosetPhotos = new ArrayList<>();
        Properties properties = load(propertiesFile);
        populate(properties);
    }

    private Properties load(File propertiesFile) {
        Properties properties = new Properties();

        try (InputStream in = new FileInputStream(propertiesFile)) {
            properties.load(in);
        } catch (IOException e) {
            throw new FlickrRuntimeException("Problem loading properties", e);
        }

        return properties;
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

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getTokenSecret() {
        return tokenSecret;
    }

    @Override
    public String getNsid() {
        return nsid;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getTestGroupId() {
        return testGroupId;
    }

    @Override
    public String getPhotoId() {
        return photoId;
    }

    @Override
    public String getPhotosetId() {
        return photosetId;
    }

    @Override
    public String getImageFile() {
        return imageFile;
    }

    @Override
    public String getGeoWritePhotoId() {
        return geoWritePhotoId;
    }

    @Override
    public String getCollectionId() {
        return collectionId;
    }

    @Override
    public String getCollectionUrlId() {
        return collectionUrlId;
    }

    @Override
    public String getGalleryId() {
        return galleryId;
    }

    @Override
    public String getDisplayname() {
        return displayname;
    }

    @Override
    public List<String> getPhotosetPhotos() {
        return photosetPhotos;
    }

    @Override
    public boolean isRealFlickr() {
        return true;
    }

}
