package com.flickr4java.flickr.test.util;

import java.util.List;

public interface TestProperties {
    String getHost();

    String getApiKey();

    String getSecret();

    String getToken();

    String getTokenSecret();

    String getNsid();

    String getEmail();

    String getUsername();

    String getGroupId();

    String getTestGroupId();

    String getPhotoId();

    String getPhotosetId();

    String getImageFile();

    String getGeoWritePhotoId();

    String getCollectionId();

    String getCollectionUrlId();

    String getGalleryId();

    String getDisplayname();

    List<String> getPhotosetPhotos();

    boolean isRealFlickr();
}
