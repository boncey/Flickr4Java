/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.SOAP;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.photos.Permissions;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.Photocount;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * @author Anthony Eden
 */
public class PhotosInterfaceSOAPTest {

    Flickr flickr = null;
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
            flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), soap);
        } finally {
            IOUtilities.close(in);
        }
    }

    @Ignore
    @Test
    public void testGetInfo() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Photo photo = iface.getInfo(properties.getProperty("photoid"), null);
        assertNotNull(photo);
        System.out.println("photo id: " + photo.getId());
    }

    @Ignore
    @Test
    public void testAddAndRemoveTags() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        String[] tagsToAdd = {"test"};
        iface.addTags(photoId, tagsToAdd);
        Photo photo = iface.getInfo(photoId, null);
        Collection tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(1, tags.size());

        String tagId = null;
        Iterator tagsIter = tags.iterator();
        TAG_LOOP: while (tagsIter.hasNext()) {
            Tag tag = (Tag) tagsIter.next();
            if (tag.getValue().equals("test")) {
                tagId = tag.getId();
                break TAG_LOOP;
            }
        }

        iface.removeTag(tagId);
    }

    @Ignore
    @Test
    public void testGetContactsPhotos() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection photos = iface.getContactsPhotos(0, false, false, false);
        assertNotNull(photos);
        assertTrue(photos.size() > 0);
    }

    @Ignore
    @Test
    public void testGetContactsPublicPhotos() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection photos = iface.getContactsPublicPhotos(properties.getProperty("nsid"), 0, false, false, false);
        assertNotNull(photos);
        assertTrue(photos.size() > 0);
    }

    @Ignore
    @Test
    public void testGetContext() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        PhotoContext photoContext = iface.getContext(properties.getProperty("photoid"));
        assertNotNull(photoContext);
    }

    @Ignore
    @Test
    public void testGetCounts() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Date[] dates = new Date[2];
        dates[0] = new Date(100000);
        dates[1] = new Date(); // now
        Collection counts = iface.getCounts(dates, null);
        assertNotNull(counts);

        Iterator countsIter = counts.iterator();
        while (countsIter.hasNext()) {
            Photocount photocount = (Photocount) countsIter.next();
            System.out.println("count: " + photocount.getCount());
        }
    }

    @Ignore
    @Test
    public void testGetExif() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection exifs = iface.getExif(properties.getProperty("photoid"), null);
        assertNotNull(exifs);
    }

    @Ignore
    @Test
    public void testGetNotInSet() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection photos = iface.getNotInSet(-1, -1);
        assertNotNull(photos);
    }

    @Ignore
    @Test
    public void testGetPerms() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Permissions perms = iface.getPerms(properties.getProperty("photoid"));
        assertNotNull(perms);
    }

    @Ignore
    @Test
    public void testGetRecent() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Set hSet = new HashSet();
        Collection photos = iface.getRecent(hSet, 0, 0);
        assertNotNull(photos);
    }

    @Ignore
    @Test
    public void testGetSizes() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection sizes = iface.getSizes(properties.getProperty("photoid"));
        assertNotNull(sizes);
    }

    @Ignore
    @Test
    public void testGetUntagged() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection photos = iface.getUntagged(0, 0);
        assertNotNull(photos);
    }

    @Ignore
    @Test
    public void testSearch() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        SearchParameters searchParams = new SearchParameters();
        searchParams.setUserId(properties.getProperty("nsid"));
        PhotoList photos = iface.search(searchParams, 0, 0);
        assertNotNull(photos);
        assertEquals(1, photos.getPage());
        assertEquals(1, photos.getPages());
        assertEquals(100, photos.getPerPage());
        assertEquals(3, photos.getTotal());
    }

    @Ignore
    @Test
    public void testTagSearch() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        String[] tags = {"flowers"};
        searchParameters.setTags(tags);
        Collection photos = iface.search(searchParameters, -1, -1);
        assertNotNull(photos);

    }

    @Ignore
    @Test
    public void testSetDates() {

    }

    @Ignore
    @Test
    public void testSetMeta() throws FlickrException, IOException, SAXException {
        Auth auth = flickr.getAuthInterface().checkToken(properties.getProperty("token"), properties.getProperty("tokensecret"));
        RequestContext.getRequestContext().setAuth(auth);

        String newTitle = "New Title";
        PhotosInterface iface = flickr.getPhotosInterface();
        Photo photo = iface.getInfo(properties.getProperty("photoid"), null);
        String oldTitle = photo.getTitle();
        photo.setTitle(newTitle);
        iface.setMeta(photo.getId(), photo.getTitle(), null);
        Photo updatedPhoto = iface.getInfo(properties.getProperty("photoid"), null);
        assertEquals(newTitle, updatedPhoto.getTitle());
        iface.setMeta(photo.getId(), oldTitle, null);
    }

    @Ignore
    @Test
    public void testSetPerms() {

    }

    @Ignore
    @Test
    public void testSetTags() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        String[] tagsToAdd = {"test"};
        iface.setTags(photoId, tagsToAdd);

        Photo photo = iface.getInfo(photoId, null);
        Collection tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(1, tags.size());

        //        String tagId = null;
        //        Iterator tagsIter = tags.iterator();
        //        TAG_LOOP: while (tagsIter.hasNext()) {
        //            Tag tag = (Tag) tagsIter.next();
        //            if (tag.getValue().equals("test")) {
        //                tagId = tag.getId();
        //                break TAG_LOOP;
        //            }
        //        }

        String[] tagsAfterRemove = {};
        iface.setTags(photoId, tagsAfterRemove);

        photo = iface.getInfo(photoId, null);
        tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(0, tags.size());
    }

    @Ignore
    @Test
    public void testGetSmallImage() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.SMALL);
        assertNotNull(image);
        assertEquals(240, image.getWidth());
        assertEquals(180, image.getHeight());
        //        System.out.println("Image width: " + image.getWidth());
        //        System.out.println("Image height: " + image.getHeight());
        ImageIO.write(image, "jpg", new File("out.small.jpg"));
    }

    @Ignore
    @Test
    public void testGetThumbnailImage() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.THUMB);
        assertNotNull(image);
        assertEquals(100, image.getWidth());
        assertEquals(75, image.getHeight());
        ImageIO.write(image, "jpg", new File("out.thumbnail.jpg"));
    }

    @Ignore
    @Test
    public void testGetSmallSquareImage() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.SQUARE);
        assertNotNull(image);
        assertEquals(75, image.getWidth());
        assertEquals(75, image.getHeight());
        ImageIO.write(image, "jpg", new File("out.smallsquare.jpg"));
    }

    @Ignore
    @Test
    public void testGetOriginalImage() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.ORIGINAL);
        assertNotNull(image);
        assertEquals(800, image.getWidth());
        assertEquals(600, image.getHeight());
        ImageIO.write(image, "jpg", new File("out.original.jpg"));
    }

    @Ignore
    @Test
    public void testGetMediumImage() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.MEDIUM);
        assertNotNull(image);
        assertEquals(500, image.getWidth());
        assertEquals(375, image.getHeight());
        ImageIO.write(image, "jpg", new File("out.medium.jpg"));
    }

    @Ignore
    @Test
    public void testGetLargeImage() throws FlickrException, IOException, SAXException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = properties.getProperty("photoid");
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.LARGE);
        assertNotNull(image);
        assertEquals(500, image.getWidth());
        assertEquals(375, image.getHeight());
        ImageIO.write(image, "jpg", new File("out.large.jpg"));
    }
}
