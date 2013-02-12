package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.places.Location;
import com.flickr4java.flickr.places.Place;
import com.flickr4java.flickr.places.PlaceType;
import com.flickr4java.flickr.places.PlacesInterface;
import com.flickr4java.flickr.places.PlacesList;
import com.flickr4java.flickr.tags.Tag;

/**
 * Tests for the PlacesInterface.
 * 
 * @author mago
 * @version $Id: PlacesInterfaceTest.java,v 1.11 2009/07/11 20:30:27 x-mago Exp $
 */
public class PlacesInterfaceTest extends Flickr4JavaTest {
    String sfWoeId = "2487956";

    @Test
    public void testFindByLonLat() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList<Place> list = placesInterface.findByLatLon(52.524577D, 13.412247D, Flickr.ACCURACY_CITY);
        assertTrue(list.getTotal() == 1);
        Place place = (Place) list.get(0);
        assertEquals("zot2ouJXUbKOJRM", place.getPlaceId());
        assertEquals("/Germany/Berlin/Berlin", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());
        assertEquals("638242", place.getWoeId());
        assertEquals(52.516D, place.getLatitude(), 0d);
        assertEquals(13.376D, place.getLongitude(), 0d);
    }

    @Test
    public void testFind() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList<Place> list = placesInterface.find("Alabama");
        assertTrue(list.getTotal() == 3);
        Place place = (Place) list.get(0);
        assertEquals("KSb302RTUb74OxqL", place.getPlaceId());
        assertEquals("/United+States/Alabama", place.getPlaceUrl());
        assertEquals(Place.TYPE_REGION, place.getPlaceType());

        place = (Place) list.get(1);
        assertEquals("D_36GGlTUb8R9C_Y", place.getPlaceId());
        assertEquals("/United+States/New+York/Alabama", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());

        place = (Place) list.get(2);
        assertEquals("3BCBV2pQV70Ei4_4", place.getPlaceId());
        assertEquals("/South+Africa/North-west/Alabama", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());
    }

    @Test
    public void testFind2() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList<Place> list = placesInterface.find("Europe");
        assertTrue(list.getTotal() == 2);
        Place place = (Place) list.get(0);
        assertEquals("6dCBhRRTVrJiB5xOrg", place.getPlaceId());
        assertEquals("/6dCBhRRTVrJiB5xOrg", place.getPlaceUrl());
        assertEquals(Place.TYPE_CONTINENT, place.getPlaceType());

        place = (Place) list.get(1);
        assertEquals("SmLXwKZUV7JlnVvxUA", place.getPlaceId());
        assertEquals("/France/Ile-de-France/Paris/Europe", place.getPlaceUrl());
        assertEquals(Place.TYPE_NEIGHBOURHOOD, place.getPlaceType());
    }

    @Test
    public void testResolvePlaceId() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        Location location = placesInterface.resolvePlaceId("7.MJR8tTVrIO1EgB"); // SF
        placeAssertions(location);
    }

    @Test
    public void testResolvePlaceUrl() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        Location location = placesInterface.resolvePlaceURL("/United+States/California/San+Francisco");
        placeAssertions(location);
    }

    @Test
    public void testGetChildrenWithPhotosPublic() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String woeId = "2487956";
        String placeId = "kH8dLOubBZRvX_YZ";
        PlacesList<Place> list = placesInterface.getChildrenWithPhotosPublic(placeId, woeId);
        boolean presidioFound = false;
        for (int i = 0; i < list.size(); i++) {
            Place place = (Place) list.get(i);
            // System.out.println(place.getName());
            if (place.getPlaceId().equals("uSdaoQpTUb.eNcX1Jg")) {
                assertEquals("Presidio, San Francisco, CA, US, United States", place.getName());
                presidioFound = true;
            }
        }
        assertTrue(presidioFound);
        assertTrue(list.size() > 40);
    }

    @Test
    public void testGetInfo() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String woeId = "2487956";
        String placeId = "7.MJR8tTVrIO1EgB";
        Location loc = placesInterface.getInfo(woeId, null);
        assertEquals("/United+States/California/San+Francisco", loc.getPlaceUrl());
        loc = placesInterface.getInfo(null, placeId);
        assertEquals("/United+States/California/San+Francisco", loc.getPlaceUrl());
    }

    @Test
    public void testGetInfoByUrl() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String placeId = "7.MJR8tTVrIO1EgB";
        String url = "/United+States/California/San+Francisco";
        Location loc = placesInterface.getInfoByUrl(url);
        assertEquals(loc.getPlaceId(), placeId);
    }

    @Test
    public void testGetPlaceTypes() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        List<PlaceType> placeTypes = placesInterface.getPlaceTypes();
        boolean neighbourhoodFound = false;
        boolean regionFound = false;
        for (int i = 0; i < placeTypes.size(); i++) {
            PlaceType placeType = (PlaceType) placeTypes.get(i);
            if (placeType.getPlaceTypeName().equals("neighbourhood") && placeType.getPlaceTypeId() == 22) {
                neighbourhoodFound = true;
            }
            if (placeType.getPlaceTypeName().equals("region") && placeType.getPlaceTypeId() == 8) {
                regionFound = true;
            }
            // System.out.println(placeType.getPlaceTypeName() + " " + placeType.getPlaceTypeId());
        }
        assertTrue(neighbourhoodFound);
        assertTrue(regionFound);
        assertTrue(placeTypes.size() > 5);
    }

    @Test
    public void testGetTopPlacesList() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList<Place> places = placesInterface.getTopPlacesList(Place.TYPE_COUNTRY, null, null, sfWoeId);
        assertNotNull(places);
    }

    @Test
    public void testPlacesForBoundingBox() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String bbox = "-122.42307100000001,37.773779,-122.381071,37.815779";
        int placeType = Place.TYPE_LOCALITY;
        PlacesList<Place> places = placesInterface.placesForBoundingBox(placeType, bbox);
        assertTrue((places.size() > 0));
        Place place = (Place) places.get(0);
        assertEquals(sfWoeId, place.getWoeId());
        assertEquals("7.MJR8tTVrIO1EgB", place.getPlaceId());
        assertEquals("/United+States/California/San+Francisco", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());
    }

    @Test
    public void testPlacesForContacts() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        int placeType = Place.TYPE_REGION;
        String placeId = null;
        String woeId = null;
        String threshold = null;
        String contacts = "all";
        PlacesList<Place> places = placesInterface.placesForContacts(placeType, placeId, woeId, threshold, contacts);
        assertTrue((places.size() > 0));
        for (int i = 0; i < places.size(); i++) {
            Place place = (Place) places.get(i);
            assertTrue(place.getPhotoCount() > 0);
            // System.out.println(place.getName() + " " + place.getPlaceUrl());
        }
    }

    @Test
    public void testPlacesForTags() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        int placeTypeId = Place.TYPE_REGION;
        String placeId = null;
        String threshold = "4";
        String[] tags = { "sunny", "urban" };
        String tagMode = "any";
        String machineTags = null;
        String machineTagMode = null;
        Calendar minUploadDate = Calendar.getInstance();
        Calendar maxUploadDate = Calendar.getInstance();
        Calendar minTakenDate = Calendar.getInstance();
        Calendar maxTakenDate = Calendar.getInstance();
        minUploadDate.roll(Calendar.YEAR, -3);
        minTakenDate.roll(Calendar.YEAR, -3);
        PlacesList<Place> places = placesInterface.placesForTags(placeTypeId, sfWoeId, placeId, threshold, tags, tagMode, machineTags, machineTagMode,
                minUploadDate.getTime(), maxUploadDate.getTime(), minTakenDate.getTime(), maxTakenDate.getTime());
        assertTrue((places.size() == 1));
        Place place = (Place) places.get(0);
        assertEquals("NsbUWfBTUb4mbyVu", place.getPlaceId());
        assertEquals(Place.TYPE_REGION, place.getPlaceType());
        assertEquals("/United+States/California", place.getPlaceUrl());
    }

    @Test
    public void testPlacesForUser() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        int placeType = Place.TYPE_REGION;
        String placeId = null;
        String woeId = null;
        String threshold = null;
        Calendar minUploadDate = Calendar.getInstance();
        Calendar maxUploadDate = Calendar.getInstance();
        Calendar minTakenDate = Calendar.getInstance();
        Calendar maxTakenDate = Calendar.getInstance();
        minUploadDate.roll(Calendar.YEAR, -3);
        minTakenDate.roll(Calendar.YEAR, -3);
        PlacesList<Place> places = placesInterface.placesForUser(placeType, woeId, placeId, threshold, minUploadDate.getTime(), maxUploadDate.getTime(),
                minTakenDate.getTime(), maxTakenDate.getTime());
        assertTrue((places.size() > 0));
        for (Place place : places) {
            assertNotNull(place);
        }
    }

    @Test
    public void testTagsForPlace() throws FlickrException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String placeId = null;
        Calendar minUploadDate = Calendar.getInstance();
        Calendar maxUploadDate = Calendar.getInstance();
        Calendar minTakenDate = Calendar.getInstance();
        Calendar maxTakenDate = Calendar.getInstance();
        minUploadDate.roll(Calendar.YEAR, -3);
        minTakenDate.roll(Calendar.YEAR, -3);
        ArrayList<Tag> tags = placesInterface.tagsForPlace(sfWoeId, placeId, minUploadDate.getTime(), maxUploadDate.getTime(), minTakenDate.getTime(),
                maxTakenDate.getTime());
        assertTrue((tags.size() > 0));
        boolean calFound = false;
        for (Tag tag : tags) {
            if (tag.getValue().equals("california") && tag.getCount() > 140000) {
                calFound = true;
            }
            // System.out.println(tag.getValue() + " " + tag.getCount());
        }
        assertTrue(calFound);
    }

    private void placeAssertions(Location location) {
        assertEquals("7.MJR8tTVrIO1EgB", location.getPlaceId());
        assertEquals("/United+States/California/San+Francisco", location.getPlaceUrl());
        assertEquals("2487956", location.getWoeId());
        assertEquals(37.779D, location.getLatitude(), 0d);
        assertEquals(-122.420D, location.getLongitude(), 0d);
        assertEquals(Place.TYPE_LOCALITY, location.getPlaceType());

        assertEquals("7.MJR8tTVrIO1EgB", location.getLocality().getPlaceId());
        assertEquals("San Francisco, California, United States", location.getLocality().getName());
        assertEquals("2487956", location.getLocality().getWoeId());
        assertEquals(37.779D, location.getLocality().getLatitude(), 0d);
        assertEquals(-122.420D, location.getLocality().getLongitude(), 0d);

        assertEquals(".7sOmlRQUL9nK.kMzA", location.getCounty().getPlaceId());
        assertEquals("San Francisco County, California, United States", location.getCounty().getName());
        assertEquals("12587707", location.getCounty().getWoeId());
        assertEquals(37.767D, location.getCounty().getLatitude(), 0d);
        assertEquals(-122.443D, location.getCounty().getLongitude(), 0d);

        assertEquals("NsbUWfBTUb4mbyVu", location.getRegion().getPlaceId());
        assertEquals("California, United States", location.getRegion().getName());
        assertEquals("2347563", location.getRegion().getWoeId());
        assertEquals(37.271D, location.getRegion().getLatitude(), 0d);
        assertEquals(-119.270D, location.getRegion().getLongitude(), 0d);

        assertEquals("nz.gsghTUb4c2WAecA", location.getCountry().getPlaceId());
        assertEquals("United States", location.getCountry().getName());
        assertEquals("23424977", location.getCountry().getWoeId());
        assertEquals(37.167D, location.getCountry().getLatitude(), 0d);
        assertEquals(-95.845D, location.getCountry().getLongitude(), 0d);
    }
}
