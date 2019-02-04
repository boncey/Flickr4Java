/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;

/**
 * @author Anthony Eden
 */
public class UploaderTest extends Flickr4JavaTest {

    private static final String DUMMY_PHOTO_ID = "1234567890";

    /**
     * Test photo uploading using a byte array.
     *
     * @throws IOException
     * @throws FlickrException
     */
    @Test
    public void testUploadByteArray() throws IOException, FlickrException {
        File imageFile = new File(testProperties.getImageFile());
        Uploader uploader = flickr.getUploader();
        PhotosInterface pint = flickr.getPhotosInterface();

        if (testProperties.isRealFlickr()) {

            UploadMetaData metaData = buildPrivatePhotoMetadata();
            // check correct handling of escaped value
            metaData.setTitle("óöä");
            String photoId = uploader.upload(Files.readAllBytes(imageFile.toPath()), metaData);
            assertNotNull(photoId);
            try {
                pint.delete(photoId);
            } catch (FlickrException e) {
                // Ignore if user doesn't have delete permissions
                // This will leave a *private* photo in the test account's photostream!
                if (!e.getErrorCode().equals("99")) {
                    throw e;
                }

            }
        } else {
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            String photoId = uploader.upload("".getBytes(), metaData);
            assertNotNull(photoId);
        }
    }

    /**
     * Test photo upload using an InputStream.
     *
     * @throws IOException
     * @throws FlickrException
     */
    @Test
    public void testUploadInputStream() throws IOException, FlickrException {
        File imageFile = new File(testProperties.getImageFile());
        Uploader uploader = flickr.getUploader();
        PhotosInterface pint = flickr.getPhotosInterface();

        if (testProperties.isRealFlickr()) {
            try (InputStream in = new FileInputStream(imageFile)) {
                UploadMetaData metaData = buildPrivatePhotoMetadata();
                metaData.setPublicFlag(false);
                // check correct handling of escaped value
                metaData.setTitle("óöä");
                String photoId = uploader.upload(in, metaData);
                assertNotNull(photoId);
                try {
                    pint.delete(photoId);
                } catch (FlickrException e) {
                    // Ignore if user doesn't have delete permissions
                    // This will leave a *private* photo in the test account's photostream!
                    if (!e.getErrorCode().equals("99")) {
                        throw e;
                    }
                }
            }
        } else {
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            String photoId = uploader.upload("".getBytes(), metaData);
            assertNotNull(photoId);
        }
    }

    /**
     * Test photo replace using an InputStream.
     *
     * @throws IOException
     * @throws FlickrException
     */
    @Test
    public void testReplaceInputStream() throws IOException, FlickrException {
        Uploader uploader = flickr.getUploader();
        PhotosInterface pint = flickr.getPhotosInterface();
        File imageFile = new File(testProperties.getImageFile());

        if (testProperties.isRealFlickr()) {
            String photoId;
            try (InputStream uploadIS = new FileInputStream(imageFile)) {

                // Upload a photo, which we'll replace, then delete
                UploadMetaData metaData = buildPrivatePhotoMetadata();
                photoId = uploader.upload(uploadIS, metaData);
            }

            try (InputStream replaceIS = new FileInputStream(imageFile)) {

                try {
                    photoId = uploader.replace(replaceIS, photoId, false);
                    assertNotNull(photoId);
                } catch (FlickrException e) {
                    // Error code 1 means test account is not pro so don't fail test because of that
                    if (!e.getErrorCode().equals("1")) {
                        throw e;
                    }
                }
                try {
                    pint.delete(photoId);
                } catch (FlickrException e) {
                    // Ignore if user doesn't have delete permissions
                    // This will leave a *private* photo in the test account's photostream!
                    if (!e.getErrorCode().equals("99")) {
                        throw e;
                    }
                }
            }
        } else {
            try (InputStream replaceIS = new ByteArrayInputStream("".getBytes())) {
                String photoId = uploader.replace(replaceIS, DUMMY_PHOTO_ID, false);
                assertNotNull(photoId);
            }
        }
    }

    /**
     * Test photo replace using a byte array.
     *
     * @throws IOException
     * @throws FlickrException
     */
    @Test
    public void testReplaceByteArray() throws IOException, FlickrException {
        File imageFile = new File(testProperties.getImageFile());
        Uploader uploader = flickr.getUploader();
        PhotosInterface pint = flickr.getPhotosInterface();

        if (testProperties.isRealFlickr()) {

            // Upload a photo, which we'll replace, then delete
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            String photoId = uploader.upload(Files.readAllBytes(imageFile.toPath()), metaData);

            try {
                photoId = uploader.replace(Files.readAllBytes(imageFile.toPath()), photoId, false);
                assertNotNull(photoId);
            } catch (FlickrException e) {
                // Error code 1 means test account is not pro so don't fail test because of that
                if (!e.getErrorCode().equals("1")) {
                    throw e;
                }
            }

            try {
                pint.delete(photoId);
            } catch (FlickrException e) {
                // Ignore if user doesn't have delete permissions
                // This will leave a *private* photo in the test account's photostream!
                if (!e.getErrorCode().equals("99")) {
                    throw e;
                }
            }
        } else {
            String photoId = uploader.replace("".getBytes(), DUMMY_PHOTO_ID, false);
            assertNotNull(photoId);
        }
    }

    /**
     * Build {@link UploadMetaData} with public set to false so uploaded photos are private.
     *
     * @return
     */
    private UploadMetaData buildPrivatePhotoMetadata() {
        UploadMetaData uploadMetaData = new UploadMetaData();
        uploadMetaData.setPublicFlag(false);
        return uploadMetaData;
    }

}
