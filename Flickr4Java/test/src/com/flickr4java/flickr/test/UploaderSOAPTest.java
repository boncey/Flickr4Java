/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Anthony Eden
 */
public class UploaderSOAPTest {

    Uploader uploader = null;
    Properties properties = null;

    @Before
    public void setUp() throws ParserConfigurationException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            Flickr.debugStream = true;
            SOAP soap = new SOAP(properties.getProperty("host"));

            uploader = new Uploader(
                properties.getProperty("apiKey"),
                properties.getProperty("secret")
            );
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
    @Ignore
    @Test
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
     * @throws SAXException
     */
    @Ignore
    @Test
    public void testUploadInputStream() throws IOException, FlickrException, SAXException {
        File imageFile = new File(properties.getProperty("imagefile"));
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
