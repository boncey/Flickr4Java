package com.flickr4java.flickr.test.util;

import java.util.Arrays;
import java.util.List;

/**
 * TestProperties for stubbed out unit tests.
 *
 * Values are not sent to Flickr so don't have to be real.
 */
public class StubTestProperties implements TestProperties {

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public String getApiKey() {
        return "FAKE_API_KEY";
    }

    @Override
    public String getSecret() {
        return "FAKE_SECRET";
    }

    @Override
    public String getToken() {
        return "FAKE_TOKEN";
    }

    @Override
    public String getTokenSecret() {
        return "FAKE_TOKEN_SECRET";
    }

    @Override
    public String getNsid() {
        return "24573443@N04";
    }

    @Override
    public String getEmail() {
        return "EMAIL";
    }

    @Override
    public String getUsername() {
        return "boncey_test";
    }

    @Override
    public String getGroupId() {
        return "34427469792@N01";
    }

    @Override
    public String getTestGroupId() {
        return "12345";
    }

    @Override
    public String getPhotoId() {
        return "5628095432";
    }

    @Override
    public String getPhotosetId() {
        return "12345";
    }

    @Override
    public String getImageFile() {
        return "FILE";
    }

    @Override
    public String getGeoWritePhotoId() {
        return "12345";
    }

    @Override
    public String getCollectionId() {
        return "12345";
    }

    @Override
    public String getCollectionUrlId() {
        return "12345";
    }

    @Override
    public String getGalleryId() {
        return "72157634925210101";
    }

    @Override
    public String getDisplayname() {
        return "Darren Greaves";
    }

    @Override
    public List<String> getPhotosetPhotos() {
        return Arrays.asList("5628122182", "5628111146", "4169948514");
    }

    @Override
    public boolean isRealFlickr() {
        return false;
    }
}
