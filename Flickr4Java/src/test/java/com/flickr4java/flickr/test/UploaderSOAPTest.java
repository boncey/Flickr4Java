/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * @author Anthony Eden
 */
public class UploaderSOAPTest {

    Uploader uploader = null;

    private TestProperties testProperties;

    @Before
    public void setUp() throws ParserConfigurationException, IOException {
        testProperties = new TestProperties();

        Flickr.debugStream = true;
        SOAP soap = new SOAP(testProperties.getHost());

        uploader = new Uploader(testProperties.getApiKey(), testProperties.getSecret());
    }

    /**
     * Test photo uploading using a byte array.
     * 
     * @throws IOException
     * @throws FlickrException
     */
    @Ignore
    @Test
    public void testUploadByteArray() throws IOException, FlickrException {
        File imageFile = new File(testProperties.getImageFile());
        ByteArrayOutputStream out = null;
        InputStream in = null;

        try {
            in = new FileInputStream(imageFile);
            out = new ByteArrayOutputStream();
            int b = -1;
            while ((b = in.read()) != -1) {
                out.write((byte) b);
            }
            UploadMetaData metaData = new UploadMetaData();
            String photoId = uploader.upload(out.toByteArray(), metaData);
            assertNotNull(photoId);
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
     */
    @Ignore
    @Test
    public void testUploadInputStream() throws IOException, FlickrException {
        File imageFile = new File(testProperties.getImageFile());
        InputStream in = null;

        try {
            in = new FileInputStream(imageFile);
            UploadMetaData metaData = new UploadMetaData();
            String photoId = uploader.upload(in, metaData);
            assertNotNull(photoId);
        } finally {
            IOUtilities.close(in);
        }
    }

}
