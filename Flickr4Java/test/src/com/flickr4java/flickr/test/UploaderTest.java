/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Anthony Eden
 */
public class UploaderTest {

    Uploader uploader = null;
    PhotosInterface pint = null;
    Flickr flickr = null;
    private TestProperties testProperties;

    @Before
    public void setUp() throws IOException, FlickrException {
        testProperties = new TestProperties();
        Flickr.debugRequest = false;
        Flickr.debugStream = false;

        REST rest = new REST();

        flickr = new Flickr(
                testProperties.getApiKey(),
                testProperties.getSecret(),
                rest
                );
        uploader = flickr.getUploader();
        pint = flickr.getPhotosInterface();

        Auth auth = new Auth();
        auth.setPermission(Permission.WRITE);
        auth.setToken(testProperties.getToken());
        auth.setTokenSecret(testProperties.getTokenSecret());

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }

    /**
     * Test photo uploading using a byte array.
     *
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    @Test
    public void testUploadByteArray() throws IOException, FlickrException, SAXException {
        File imageFile = new File(testProperties.getImageFile());
        InputStream in = null;

        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(imageFile);
            out = new ByteArrayOutputStream();
            int b = -1;
            while ((b = in.read()) != -1) {
                out.write((byte) b);
            }
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            // check correct handling of escaped value
            metaData.setTitle("óöä");
            String photoId = uploader.upload(out.toByteArray(), metaData);
            assertNotNull(photoId);
            pint.delete(photoId);
        } finally {
            IOUtilities.close(in);
            IOUtilities.close(out);
        }
    }

    /**
     * Test photo upload using an InputStream.
     *
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    @Test
    public void testUploadInputStream() throws IOException, FlickrException, SAXException {
        File imageFile = new File(testProperties.getImageFile());
        InputStream in = null;

        try {
            in = new FileInputStream(imageFile);
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            metaData.setPublicFlag(false);
            // check correct handling of escaped value
            metaData.setTitle("óöä");
            String photoId = uploader.upload(in, metaData);
            assertNotNull(photoId);
            pint.delete(photoId);
        } finally {
            IOUtilities.close(in);
        }
    }

    /**
     * Test photo replace using an InputStream.
     *
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    @Test
    public void testReplaceInputStream() throws IOException, FlickrException, SAXException {
        File imageFile = new File(testProperties.getImageFile());
        InputStream uploadIS = null;
        String photoId = null;
        try {
            uploadIS = new FileInputStream(imageFile);

            // Upload a photo, which we'll replace, then delete
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            photoId = uploader.upload(uploadIS, metaData);
        } finally {
            IOUtilities.close(uploadIS);
        }

        InputStream replaceIS = null;
        try {
            replaceIS = new FileInputStream(imageFile);

            try {
                photoId = uploader.replace(replaceIS, photoId, false);
                assertNotNull(photoId);
            } catch (FlickrException e) {
                // Error code 1 means test account is not pro so don't fail test because of that
                if (!e.getErrorCode().equals("1")) {
                    throw e;
                }
            }
            pint.delete(photoId);
        } finally {
            IOUtilities.close(replaceIS);
        }
    }

    /**
     * Test photo replace using a byte array.
     *
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    @Test
    public void testReplaceByteArray() throws IOException, FlickrException, SAXException {
        File imageFile = new File(testProperties.getImageFile());
        InputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            in = new FileInputStream(imageFile);
            out = new ByteArrayOutputStream();
            int b = -1;
            while ((b = in.read()) != -1) {
                out.write((byte) b);
            }

            // Upload a photo, which we'll replace, then delete
            UploadMetaData metaData = buildPrivatePhotoMetadata();
            String photoId = uploader.upload(out.toByteArray(), metaData);

            try {
                photoId = uploader.replace(out.toByteArray(), photoId, false);
                assertNotNull(photoId);
            } catch (FlickrException e) {
                // Error code 1 means test account is not pro so don't fail test because of that
                if (!e.getErrorCode().equals("1")) {
                    throw e;
                }
            }

            pint.delete(photoId);
        } finally {
            IOUtilities.close(in);
        }
    }

    /**
     * Build {@link UploadMetaData} with public set to false so uploaded photos are private.
     * @return
     */
    private UploadMetaData buildPrivatePhotoMetadata() {
        UploadMetaData uploadMetaData = new UploadMetaData();
        uploadMetaData.setPublicFlag(false);
        return uploadMetaData;
    }

}
