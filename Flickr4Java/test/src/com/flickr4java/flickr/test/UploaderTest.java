/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

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

import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Anthony Eden
 */
public class UploaderTest extends TestCase {

    Uploader uploader = null;
    PhotosInterface pint = null;
    Flickr flickr = null;
    Properties properties = null;

    @Override
    public void setUp() throws IOException, FlickrException {
        InputStream in = null;
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            REST rest = new REST();

            flickr = new Flickr(
                    properties.getProperty("apiKey"),
                    properties.getProperty("secret"),
                    rest
                    );
            uploader = flickr.getUploader();
            pint = flickr.getPhotosInterface();

            Auth auth = new Auth();
            auth.setPermission(Permission.WRITE);
            auth.setToken(properties.getProperty("token"));
            auth.setTokenSecret(properties.getProperty("tokensecret"));

            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setAuth(auth);
            flickr.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    /**
     * Test photo uploading using a byte array.
     *
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public void testUploadByteArray() throws IOException, FlickrException, SAXException {
        File imageFile = new File(properties.getProperty("imagefile"));
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
    public void testUploadInputStream() throws IOException, FlickrException, SAXException {
        File imageFile = new File(properties.getProperty("imagefile"));
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
    public void testReplaceInputStream() throws IOException, FlickrException, SAXException {
        File imageFile = new File(properties.getProperty("imagefile"));
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
    public void testReplaceByteArray() throws IOException, FlickrException, SAXException {
        File imageFile = new File(properties.getProperty("imagefile"));
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
