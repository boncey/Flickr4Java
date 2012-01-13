/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class UploaderTest extends TestCase {

    Uploader uploader = null;
    PhotosInterface pint = null;
    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
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

            RequestContext requestContext = RequestContext.getRequestContext();

            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            auth.setPermission(Permission.DELETE);
            requestContext.setAuth(auth);
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
            UploadMetaData metaData = new UploadMetaData();
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
            UploadMetaData metaData = new UploadMetaData();
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
            UploadMetaData metaData = new UploadMetaData();
            photoId = uploader.upload(uploadIS, metaData);
        } finally {
            IOUtilities.close(uploadIS);
        }

        InputStream replaceIS = null;
        try {
            replaceIS = new FileInputStream(imageFile);

            photoId = uploader.replace(replaceIS, photoId, false);
            assertNotNull(photoId);
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
            UploadMetaData metaData = new UploadMetaData();
            String photoId = uploader.upload(out.toByteArray(), metaData);

            photoId = uploader.replace(out.toByteArray(), photoId, false);
            assertNotNull(photoId);
            pint.delete(photoId);
        } finally {
            IOUtilities.close(in);
        }
    }

}
