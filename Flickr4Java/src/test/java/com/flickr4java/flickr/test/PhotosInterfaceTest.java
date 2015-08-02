/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.Permissions;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.Photocount;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.tags.Tag;

import org.junit.After;
import org.junit.Test;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Anthony Eden
 * @version $Id: PhotosInterfaceTest.java,v 1.20 2009/07/23 21:49:35 x-mago Exp $
 */
public class PhotosInterfaceTest extends Flickr4JavaTest {

    private final File largeFile = new File("out.large.jpg");

    private final File mediumFile = new File("out.medium.jpg");

    private final File smallFile = new File("out.small.jpg");

    private final File smallSquareFile = new File("out.smallsquare.jpg");

    private final File thumbnailFile = new File("out.thumbnail.jpg");

    @After
    public void teardown() {

        largeFile.deleteOnExit();
        mediumFile.deleteOnExit();
        smallFile.deleteOnExit();
        smallSquareFile.deleteOnExit();
        thumbnailFile.deleteOnExit();
    }

    @Test
    public void testAddAndRemoveTags() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getInfo(photoId, null);

        // Find number of existing tags
        int preCount = photo.getTags().size();
        int postCount = preCount + 1;

        // Add a tag
        String[] tagsToAdd = { "test" };
        iface.addTags(photoId, tagsToAdd);

        // Check that it was added
        photo = iface.getInfo(photoId, null);
        Collection<Tag> tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(postCount, tags.size());

        // Get the added tag's ID
        String tagId = null;
        for (Tag tag : tags) {
            if (tag.getValue().equals("test")) {
                tagId = tag.getId();
                break;
            }
        }

        // Remove and check that it was removed
        iface.removeTag(tagId);
        photo = iface.getInfo(photoId, null);
        tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(preCount, tags.size());
    }

    @Test
    public void testGetInfo() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Photo photo = iface.getInfo(testProperties.getPhotoId(), null);
        assertNotNull(photo);
        assertNotNull(photo.getUrl());
        assertNotNull(photo.getTitle());
        assertNotNull(photo.getTitle());
        assertEquals(testProperties.getPhotoId(), photo.getId());
        assertNotNull(photo.getSecret());
        assertNotNull(photo.getServer());
        assertNotNull(photo.getFarm());
        assertEquals("0", photo.getLicense());
        assertEquals("jpg", photo.getOriginalFormat());
        // no pro, no original :-(
        assertEquals("", photo.getIconServer());
        assertEquals("", photo.getIconFarm());
        assertFalse(photo.isFavorite());
        // assertTrue(photo.getViews() > -1);

        User owner = photo.getOwner();
        assertEquals(testProperties.getNsid(), owner.getId());
        assertEquals(testProperties.getUsername(), owner.getUsername());

        List<Tag> tags = (List<Tag>) photo.getTags();
        assertEquals("green", (tags.get(0)).getValue());
        assertEquals("grn", (tags.get(1)).getValue());
    }

    @Test
    public void testGetContactsPhotos() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        PhotoList<Photo> photos = iface.getContactsPhotos(0, false, false, false);
        assertNotNull(photos);
        assertTrue(photos.size() > 0);
    }

    @Test
    public void testGetContactsPublicPhotos() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        PhotoList<Photo> photos = iface.getContactsPublicPhotos(testProperties.getNsid(), 0, false, false, false);
        assertNotNull(photos);
        assertTrue(photos.size() > 0);
    }

    @Test
    public void testGetContext() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        PhotoContext photoContext = iface.getContext(testProperties.getPhotoId());
        assertNotNull(photoContext);
    }

    @Test
    public void testGetCounts() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Date[] dates = new Date[2];
        dates[0] = new Date(100000);
        dates[1] = new Date(); // now
        Collection<Photocount> counts = iface.getCounts(dates, null);
        assertNotNull(counts);

        for (Photocount photocount : counts) {
            assertNotNull(photocount);
        }
    }

    @Test
    public void testGetExif() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection<Exif> exifs = iface.getExif(testProperties.getPhotoId(), null);
        assertNotNull(exifs);
    }

    @Test
    public void testGetNotInSet() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        PhotoList<Photo> photos = iface.getNotInSet(-1, -1);
        assertNotNull(photos);
    }

    @Test
    public void testGetPerms() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Permissions perms = iface.getPerms(testProperties.getPhotoId());
        assertNotNull(perms);
    }

    @Test
    public void testGetRecent() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Set<String> extras = new HashSet<String>();
        PhotoList<Photo> photos = iface.getRecent(extras, 0, 0);
        assertNotNull(photos);
    }

    @Test
    public void testGetSizes() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        Collection<Size> sizes = iface.getSizes(testProperties.getPhotoId());
        assertNotNull(sizes);
    }

    @Test
    public void testGetUntagged() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        PhotoList<Photo> photos = iface.getUntagged(0, 0);
        assertNotNull(photos);
    }

    @Test
    public void testSearch() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        SearchParameters searchParams = new SearchParameters();
        searchParams.setUserId(testProperties.getNsid());
        PhotoList<Photo> photos = iface.search(searchParams, 33, 0);
        assertNotNull(photos);
        assertEquals(1, photos.getPage());
        assertTrue(photos.getPages() >= 1);
        assertTrue(photos.getPerPage() >= 1);
        assertTrue(photos.getTotal() >= 1);
    }

    @Test
    public void testBoundingBoxSearch() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setBBox("-122.9", "45.0", "-122.0", "45.9");
        PhotoList<Photo> photos = iface.search(searchParameters, -1, -1);
        assertNotNull(photos);
    }

    @Test
    public void testRadialGeoSearch() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLatitude("45.521694");
        searchParameters.setLongitude("-122.691806");
        searchParameters.setRadius(2);
        searchParameters.setRadiusUnits("km");
        PhotoList<Photo> photos = iface.search(searchParameters, -1, -1);
        assertNotNull(photos);
    }

    @Test
    public void testTagSearch() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        String[] tags = { "flowers" };
        searchParameters.setTags(tags);
        PhotoList<Photo> photos = iface.search(searchParameters, -1, -1);
        assertNotNull(photos);
    }

    @Test
    public void testSetContentType() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        iface.setContentType(testProperties.getPhotoId(), Flickr.CONTENTTYPE_PHOTO);
    }

    @Test
    public void testSetDates() {

    }

    @Test
    public void testSetMeta() throws FlickrException {
        String newTitle = "New Title";
        PhotosInterface iface = flickr.getPhotosInterface();
        Photo photo = iface.getInfo(testProperties.getPhotoId(), null);
        String oldTitle = photo.getTitle();
        photo.setTitle(newTitle);
        iface.setMeta(photo.getId(), photo.getTitle(), null);
        Photo updatedPhoto = iface.getInfo(testProperties.getPhotoId(), null);
        assertEquals(newTitle, updatedPhoto.getTitle());
        iface.setMeta(photo.getId(), oldTitle, "Description");
    }

    @Test
    public void testSetSafetyLevel() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        iface.setSafetyLevel(testProperties.getPhotoId(), Flickr.SAFETYLEVEL_SAFE, new Boolean(false));
    }

    @Test
    public void testSetTags() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();

        String[] tagsAfterRemove = {};
        iface.setTags(photoId, tagsAfterRemove);

        Photo photo = iface.getInfo(photoId, null);
        Collection<Tag> tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(0, tags.size());

        String[] tagsToAdd = { "green", "grn", "grngrn" };
        iface.setTags(photoId, tagsToAdd);

        photo = iface.getInfo(photoId, null);
        tags = photo.getTags();
        assertNotNull(tags);
        assertEquals(3, tags.size());

        // String tagId = null;
        // Iterator tagsIter = tags.iterator();
        // TAG_LOOP: while (tagsIter.hasNext()) {
        // Tag tag = (Tag) tagsIter.next();
        // if (tag.getValue().equals("test")) {
        // tagId = tag.getId();
        // break TAG_LOOP;
        // }
        // }

    }

    @Test
    public void testGetSmallImage() throws FlickrException, IOException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.SMALL);
        assertNotNull(image);
        assertNotNull(image.getWidth());
        assertNotNull(image.getHeight());
        // System.out.println("Image width: " + image.getWidth());
        // System.out.println("Image height: " + image.getHeight());
        ImageIO.write(image, "jpg", smallFile);
    }

    @Test
    public void testGetThumbnailImage() throws FlickrException, IOException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.THUMB);
        assertNotNull(image);
        assertTrue(67 == image.getWidth() || 68 == image.getWidth());
        assertEquals(100, image.getHeight());
        ImageIO.write(image, "jpg", thumbnailFile);
    }

    @Test
    public void testGetSmallSquareImage() throws FlickrException, IOException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.SQUARE);
        assertNotNull(image);
        assertEquals(75, image.getWidth());
        assertEquals(75, image.getHeight());
        ImageIO.write(image, "jpg", smallSquareFile);
    }

    @Test
    public void testGetMediumImage() throws FlickrException, IOException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.MEDIUM);
        assertNotNull(image);
        assertNotNull(image.getWidth());
        assertNotNull(image.getHeight());
        ImageIO.write(image, "jpg", mediumFile);
    }

    @Test
    public void testGetLargeImage() throws FlickrException, IOException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getInfo(photoId, null);
        BufferedImage image = iface.getImage(photo, Size.LARGE);
        assertNotNull(image);
        assertNotNull(image.getWidth());
        assertNotNull(image.getHeight());
        ImageIO.write(image, "jpg", largeFile);
    }

    @Test
    public void testGetPhoto() throws FlickrException {
        PhotosInterface iface = flickr.getPhotosInterface();
        String photoId = testProperties.getPhotoId();
        Photo photo = iface.getPhoto(photoId, null);
        assertNotNull(photo);
    }

    /**
     * Testing the generation of URLs and the overriding by setSizes().
     */
    @Test
    public void testSetSizes() {
        List<Size> sizes = new ArrayList<Size>();
        Size size = new Size();
        size.setLabel("Thumbnail");
        size.setWidth("100");
        size.setHeight("75");
        size.setSource("urlThumb");
        size.setUrl("urlThumbPage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Square");
        size.setWidth("75");
        size.setHeight("75");
        size.setSource("urlSquare");
        size.setUrl("urlSquarePage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Small");
        size.setWidth("240");
        size.setHeight("180");
        size.setSource("urlSmall");
        size.setUrl("urlSmallPage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Medium");
        size.setWidth("240");
        size.setHeight("180");
        size.setSource("urlMedium");
        size.setUrl("urlMediumPage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Original");
        size.setWidth("240");
        size.setHeight("180");
        size.setSource("urlOriginal");
        size.setUrl("urlOriginalPage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Large");
        size.setWidth("240");
        size.setHeight("180");
        size.setSource("urlLarge");
        size.setUrl("urlLargePage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Large Square");
        size.setWidth("150");
        size.setHeight("150");
        size.setSource("urlSquareLarge");
        size.setUrl("urlSquareLargePage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Small 320");
        size.setWidth("320");
        size.setHeight("240");
        size.setSource("urlSmall320");
        size.setUrl("urlSmall320Page");
        sizes.add(size);
        size = new Size();
        size.setLabel("Medium 640");
        size.setWidth("640");
        size.setHeight("320");
        size.setSource("urlMedium640");
        size.setUrl("urlMedium640Page");
        sizes.add(size);
        size = new Size();
        size.setLabel("Medium 800");
        size.setWidth("800");
        size.setHeight("500");
        size.setSource("urlMedium800");
        size.setUrl("urlMedium800Page");
        sizes.add(size);
        size = new Size();
        size.setLabel("Large 1600");
        size.setWidth("1600");
        size.setHeight("1024");
        size.setSource("urlLarge1600");
        size.setUrl("urlLarge1600Page");
        sizes.add(size);
        size = new Size();
        size.setLabel("Large 2048");
        size.setWidth("2048");
        size.setHeight("1600");
        size.setSource("urlLarge2048");
        size.setUrl("urlLarge2048Page");
        sizes.add(size);
        size = new Size();
        size.setLabel("Video Player");
        size.setWidth("320");
        size.setHeight("240");
        size.setSource("urlVideoPlayer");
        size.setUrl("urlVideoPlayerPage");
        sizes.add(size);
        size = new Size();
        size.setLabel("Site MP4");
        size.setWidth("320");
        size.setHeight("240");
        size.setSource("urlSiteMP4");
        size.setUrl("urlSiteMP4Page");
        sizes.add(size);
        size = new Size();
        size.setLabel("Video Original");
        size.setWidth("960");
        size.setHeight("720");
        size.setSource("urlVideoOriginal");
        size.setUrl("urlVideoOriginalPage");
        sizes.add(size);

        Photo p = new Photo();
        p.setId("id");
        p.setServer("server");
        p.setSecret("secret");
        p.setOriginalSecret("osecret");
        p.setFarm("1");

        assertEquals("https://farm1.static.flickr.com/server/id_secret_m.jpg", p.getSmallUrl());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_s.jpg", p.getSmallSquareUrl());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_t.jpg", p.getThumbnailUrl());
        assertEquals("https://farm1.static.flickr.com/server/id_secret.jpg", p.getMediumUrl());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_b.jpg", p.getLargeUrl());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_q.jpg", p.getSquareLargeUrl());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_n.jpg", p.getSmall320Url());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_z.jpg", p.getMedium640Url());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_c.jpg", p.getMedium800Url());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_h.jpg", p.getLarge1600Url());
        assertEquals("https://farm1.static.flickr.com/server/id_secret_k.jpg", p.getLarge2048Url());
        try {
            assertEquals("https://farm1.static.flickr.com/server/id_osecret_o.jpg", p.getOriginalUrl());
        } catch (FlickrException ex) {
        }
        // setSizes() to override the generated URLs.
        p.setSizes(sizes);
        assertEquals("urlSmall", p.getSmallUrl());
        assertEquals("urlSquare", p.getSmallSquareUrl());
        assertEquals("urlThumb", p.getThumbnailUrl());
        assertEquals("urlMedium", p.getMediumUrl());
        assertEquals("urlLarge", p.getLargeUrl());
        assertEquals("urlSquareLarge", p.getSquareLargeUrl());
        assertEquals("urlSmall320", p.getSmall320Url());
        assertEquals("urlMedium640", p.getMedium640Url());
        assertEquals("urlMedium800", p.getMedium800Url());
        assertEquals("urlLarge1600", p.getLarge1600Url());
        assertEquals("urlLarge2048", p.getLarge2048Url());
        assertEquals("urlVideoPlayer", p.getVideoPlayerUrl());
        assertEquals("urlSiteMP4", p.getSiteMP4Url());
        assertEquals("urlVideoOriginal", p.getVideoOriginalUrl());
        try {
            assertEquals("urlOriginal", p.getOriginalUrl());
        } catch (FlickrException ex) {
        }
    }
}
