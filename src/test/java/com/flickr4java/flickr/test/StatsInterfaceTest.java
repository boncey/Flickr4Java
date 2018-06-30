package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.stats.Csv;
import com.flickr4java.flickr.stats.Domain;
import com.flickr4java.flickr.stats.DomainList;
import com.flickr4java.flickr.stats.Referrer;
import com.flickr4java.flickr.stats.ReferrerList;
import com.flickr4java.flickr.stats.Stats;
import com.flickr4java.flickr.stats.StatsInterface;
import com.flickr4java.flickr.stats.StatsSort;
import com.flickr4java.flickr.stats.Totals;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author Darren Greaves
 * 
 *         Most users don't have stats now so tests are set to disabled for now.
 */
@Ignore
public class StatsInterfaceTest extends Flickr4JavaTest {

    private final Date today = new Date();

    @Test
    public void testGetCollectionDomains() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        DomainList domains = iface.getCollectionDomains(today, null, 50, 0);
        assertNotNull(domains);

        assertEquals(1, domains.getPage());
        assertEquals(50, domains.getPerPage());

        for (Domain domain : domains) {
            assertNotNull(domain.getName());
            assertNotNull(domain.getViews());
        }
    }

    @Test
    public void testGetCollectionReferrers() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        ReferrerList referrers = iface.getCollectionReferrers(today, "flickr.com", null, 50, 0);
        assertNotNull(referrers);

        assertEquals(1, referrers.getPage());
        assertEquals(50, referrers.getPerPage());

        for (Referrer referrer : referrers) {
            assertNotNull(referrer.getUrl());
            assertNotNull(referrer.getViews());
        }
    }

    @Test
    public void testGetCollectionStats() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        Stats stats = iface.getCollectionStats(testProperties.getCollectionUrlId(), today);
        assertNotNull(stats);

        assertTrue(stats.getViews() >= 0);
        assertTrue(stats.getFavorites() >= 0);
        assertTrue(stats.getComments() >= 0);
    }

    @Test
    public void testGetPhotoDomains() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        DomainList domains = iface.getPhotoDomains(today, null, 50, 0);
        assertNotNull(domains);

        assertEquals(1, domains.getPage());
        assertEquals(50, domains.getPerPage());

        for (Domain domain : domains) {
            assertNotNull(domain.getName());
            assertNotNull(domain.getViews());
        }
    }

    @Test
    public void testGetPhotoReferrers() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        ReferrerList referrers = iface.getPhotoReferrers(today, "flickr.com", null, 50, 0);
        assertNotNull(referrers);

        assertEquals(1, referrers.getPage());
        assertEquals(50, referrers.getPerPage());
        assertEquals("flickr.com", referrers.getName());

        for (Referrer referrer : referrers) {
            assertNotNull(referrer.getUrl());
            assertNotNull(referrer.getViews());
        }
    }

    @Test
    public void testGetPhotoStats() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        Stats stats = iface.getPhotoStats(testProperties.getPhotoId(), today);
        assertNotNull(stats);

        assertTrue(stats.getViews() >= 0);
        assertTrue(stats.getFavorites() >= 0);
        assertTrue(stats.getComments() >= 0);
    }

    @Test
    public void testGetPhotosetDomains() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        DomainList domains = iface.getPhotosetDomains(today, null, 50, 0);
        assertNotNull(domains);

        assertEquals(1, domains.getPage());
        assertEquals(50, domains.getPerPage());

        for (Domain domain : domains) {
            assertNotNull(domain.getName());
            assertNotNull(domain.getViews());
        }
    }

    @Test
    public void testGetPhotosetReferrers() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        ReferrerList referrers = iface.getPhotosetReferrers(today, "flickr.com", null, 50, 0);
        assertNotNull(referrers);

        assertEquals(1, referrers.getPage());
        assertEquals(50, referrers.getPerPage());

        for (Referrer referrer : referrers) {
            assertNotNull(referrer.getUrl());
            assertNotNull(referrer.getViews());
        }
    }

    @Test
    public void testGetPhotosetStats() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        Stats stats = iface.getPhotosetStats(testProperties.getPhotosetId(), today);
        assertNotNull(stats);

        assertTrue(stats.getViews() >= 0);
        assertTrue(stats.getFavorites() >= 0);
        assertTrue(stats.getComments() >= 0);
    }

    @Test
    public void testGetPhotostreamDomains() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        DomainList domains = iface.getPhotostreamDomains(today, 50, 0);
        assertNotNull(domains);

        assertEquals(1, domains.getPage());
        assertEquals(50, domains.getPerPage());

        for (Domain domain : domains) {
            assertNotNull(domain.getName());
            assertNotNull(domain.getViews());
        }
    }

    @Test
    public void testGetPhotostreamReferrers() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        ReferrerList referrers = iface.getPhotostreamReferrers(today, "flickr.com", 50, 0);
        assertNotNull(referrers);

        assertEquals(1, referrers.getPage());
        assertEquals(50, referrers.getPerPage());

        for (Referrer referrer : referrers) {
            assertNotNull(referrer.getUrl());
            assertNotNull(referrer.getViews());
        }
    }

    @Test
    public void testGetPhotostreamStats() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        Stats stats = iface.getPhotostreamStats(today);
        assertNotNull(stats);

        assertTrue(stats.getViews() >= 0);
        assertTrue(stats.getFavorites() >= 0);
        assertTrue(stats.getComments() >= 0);
    }

    @Test
    public void testGetCsvFiles() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        List<Csv> csvFiles = iface.getCSVFiles();
        assertNotNull(csvFiles);

        assertTrue(csvFiles.size() > 0);

        for (Csv csv : csvFiles) {
            assertNotNull(csv.getHref());
            assertNotNull(csv.getType());
            assertNotNull(csv.getDate());
        }

    }

    @Test
    public void testGetPopularPhotos() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        List<Photo> photos = iface.getPopularPhotos(null, StatsSort.comments, 5, 0);

        assertNotNull(photos);

        assertTrue(photos.size() > 0);

        for (Photo photo : photos) {

            assertNotNull(photo.getId());
            assertNotNull(photo.getOwner());
            assertNotNull(photo.getSecret());
            assertNotNull(photo.getServer());
            assertNotNull(photo.getStats());

            // These may not pass for all photos
            assertTrue(photo.getStats().getComments() > 0);
            assertTrue(photo.getStats().getFavorites() > 0);
            assertTrue(photo.getStats().getViews() > 0);

        }
    }

    @Test
    public void testGetTotalViews() throws FlickrException {
        StatsInterface iface = flickr.getStatsInterface();
        Totals totals = iface.getTotalViews(null);

        assertNotNull(totals);

        assertTrue(totals.getTotal() > 0);
        assertTrue(totals.getPhotos() > 0);
        assertTrue(totals.getPhotostream() > 0);
        assertTrue(totals.getSets() > 0);
        assertTrue(totals.getCollections() > 0);

    }
}
