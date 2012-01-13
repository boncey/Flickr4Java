package com.flickr4java.flickr.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.flickr4java.flickr.places.Location;
import com.flickr4java.flickr.places.Place;
import com.flickr4java.flickr.places.PlaceType;
import com.flickr4java.flickr.places.PlacesInterface;
import com.flickr4java.flickr.places.PlacesList;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.util.IOUtilities;

/**
 * Tests for the PlacesInterface.
 *
 * @author mago
 * @version $Id: PlacesInterfaceTest.java,v 1.11 2009/07/11 20:30:27 x-mago Exp $
 */
public class PlacesInterfaceTest extends TestCase {
    String sfWoeId = "2487956";

    Flickr flickr = null;
    Properties properties = null;

    public void setUp() throws
      ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
        InputStream in = null;
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

            RequestContext requestContext = RequestContext.getRequestContext();
            AuthInterface authInterface = flickr.getAuthInterface();
            Auth auth = authInterface.checkToken(properties.getProperty("token"));
            auth.setPermission(Permission.READ);
            requestContext.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    public void testFindByLonLat()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList list = placesInterface.findByLatLon(
            52.524577D,
            13.412247D,
            Flickr.ACCURACY_CITY
        );
        assertTrue(list.getTotal() == 1);
        Place place = (Place) list.get(0);
        assertEquals("sRdiycKfApRGrrU", place.getPlaceId());
        assertEquals("/Germany/Berlin/Berlin", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());
        assertEquals("638242", place.getWoeId());
        assertEquals(52.516D, place.getLatitude());
        assertEquals(13.376D, place.getLongitude());
    }

    public void testFind()      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList list = placesInterface.find("Alabama");
        assertTrue(list.getTotal() == 3);
        Place place = (Place) list.get(0);
        assertEquals("VrrjuESbApjeFS4.", place.getPlaceId());
        assertEquals("/United+States/Alabama", place.getPlaceUrl());
        assertEquals(Place.TYPE_REGION, place.getPlaceType());

        place = (Place) list.get(1);
        assertEquals("cGHuc0mbApmzEHoP", place.getPlaceId());
        assertEquals("/United+States/New+York/Alabama", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());

        place = (Place) list.get(2);
        assertEquals("o4yVPEqYBJvFMP8Q", place.getPlaceId());
        assertEquals("/South+Africa/North-west/Alabama", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());
    }

    public void testFind2()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList list = placesInterface.find("Europe");
        assertTrue(list.getTotal() == 2);
        Place place = (Place) list.get(0);
        assertEquals("lkyV7jSbBZTkl7Wkqg", place.getPlaceId());
        assertEquals("/lkyV7jSbBZTkl7Wkqg", place.getPlaceUrl());
        assertEquals(Place.TYPE_CONTINENT, place.getPlaceType());

        place = (Place) list.get(1);
        assertEquals("Nf7Dq4acBJTgBHuaOQ", place.getPlaceId());
        assertEquals("/France/Ile-de-France/Paris/Europe", place.getPlaceUrl());
        assertEquals(Place.TYPE_NEIGHBOURHOOD, place.getPlaceType());
    }

    public void testResolvePlaceId()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        Location location = placesInterface.resolvePlaceId("kH8dLOubBZRvX_YZ"); // SF
        placeAssertions(location);
    }

    public void testResolvePlaceUrl()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        Location location = placesInterface.resolvePlaceURL("/United+States/California/San+Francisco");
        placeAssertions(location);
    }

    public void testGetChildrenWithPhotosPublic()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String woeId = "2487956";
        String placeId = "kH8dLOubBZRvX_YZ";
        PlacesList list = placesInterface.getChildrenWithPhotosPublic(placeId, woeId);
        boolean presidioFound = false;
        for (int i = 0; i < list.size(); i++) {
            Place place = (Place) list.get(i);
            //System.out.println(place.getName());
            if (place.getPlaceId().equals("xrtOyiqbApl7whEZfA")) {
                assertEquals("Presidio, San Francisco, CA, US, United States",place.getName());
                presidioFound = true;
            }
        }
        assertTrue(presidioFound);
        assertTrue(list.size() > 40);
    }

    public void testGetInfo()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String woeId = "2487956";
        String placeId = "kH8dLOubBZRvX_YZ";
        Location loc = placesInterface.getInfo(woeId, null);
        assertEquals("/United+States/California/San+Francisco",loc.getPlaceUrl());
        loc = placesInterface.getInfo(null, placeId);
        assertEquals("/United+States/California/San+Francisco",loc.getPlaceUrl());
    }

    public void testGetInfoByUrl()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String woeId = "2487956";
        String placeId = "kH8dLOubBZRvX_YZ";
        String url = "/United+States/California/San+Francisco";
        Location loc = placesInterface.getInfoByUrl(url);
        assertEquals(loc.getPlaceId(), placeId);
    }

    public void testGetPlaceTypes()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        ArrayList placeTypes = placesInterface.getPlaceTypes();
        boolean neighbourhoodFound = false;
        boolean regionFound = false;
        for (int i = 0; i < placeTypes.size(); i++) {
            PlaceType placeType = (PlaceType) placeTypes.get(i);
            if (placeType.getPlaceTypeName().equals("neighbourhood") &&
                placeType.getPlaceTypeId() == 22) {
                neighbourhoodFound = true;
            }
            if (placeType.getPlaceTypeName().equals("region") &&
                placeType.getPlaceTypeId() == 8) {
                regionFound = true;
            }
            //System.out.println(placeType.getPlaceTypeName() + " " + placeType.getPlaceTypeId());
        }
        assertTrue(neighbourhoodFound);
        assertTrue(regionFound);
        assertTrue(placeTypes.size() > 5);
    }

    public void testGetTopPlacesList() throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        PlacesList places = placesInterface.getTopPlacesList(Place.TYPE_COUNTRY, null, null, sfWoeId);
        assertNotNull(places);
    }

    public void testPlacesForBoundingBox()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String bbox = "-122.42307100000001,37.773779,-122.381071,37.815779";
        int placeType = Place.TYPE_LOCALITY;
        PlacesList places = placesInterface.placesForBoundingBox(placeType, bbox);
        assertTrue((places.size() > 0));
        Place place = (Place) places.get(0);
        assertEquals(sfWoeId, place.getWoeId());
        assertEquals("kH8dLOubBZRvX_YZ", place.getPlaceId());
        assertEquals("/United+States/California/San+Francisco", place.getPlaceUrl());
        assertEquals(Place.TYPE_LOCALITY, place.getPlaceType());
    }

    public void testPlacesForContacts()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        int placeType = Place.TYPE_REGION;
        String placeId = null;
        String woeId = null;
        String threshold = null;
        String contacts = "all";
        PlacesList places = placesInterface.placesForContacts(placeType, placeId, woeId, threshold, contacts);
        assertTrue((places.size() > 0));
        for (int i = 0; i < places.size(); i++) {
            Place place = (Place) places.get(i);
            assertTrue(place.getPhotoCount() > 0);
            //System.out.println(place.getName() + " " + place.getPlaceUrl());
        }
    }

    public void testPlacesForTags() throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        int placeTypeId = Place.TYPE_REGION;
        String placeId = null;
        String threshold = "4";
        String[] tags = {"sunny", "urban"};        
        String tagMode = "any";
        String machineTags = null;
        String machineTagMode = null;
        Calendar minUploadDate = Calendar.getInstance();
        Calendar maxUploadDate = Calendar.getInstance();
        Calendar minTakenDate = Calendar.getInstance();
        Calendar maxTakenDate = Calendar.getInstance();
        minUploadDate.roll(Calendar.YEAR, -3);
        minTakenDate.roll(Calendar.YEAR, -3);
        PlacesList places = placesInterface.placesForTags(
            placeTypeId,
            sfWoeId,
            placeId,
            threshold,
            tags,
            tagMode,
            machineTags,
            machineTagMode,
            minUploadDate.getTime(), maxUploadDate.getTime(),
            minTakenDate.getTime(), maxTakenDate.getTime()
        );
        assertTrue((places.size() == 1));
        Place place = (Place) places.get(0);
        assertEquals("SVrAMtCbAphCLAtP", place.getPlaceId());
        assertEquals(Place.TYPE_REGION, place.getPlaceType());
        assertEquals("/United+States/California", place.getPlaceUrl());
    }

    public void testPlacesForUser()
      throws FlickrException, IOException, SAXException {
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
        PlacesList places = placesInterface.placesForUser(
            placeType, woeId, placeId, threshold,
            minUploadDate.getTime(), maxUploadDate.getTime(),
            minTakenDate.getTime(), maxTakenDate.getTime()
        );
        assertTrue((places.size() == 0));
        for (int i = 0; i < places.size(); i++) {
            Place place = (Place) places.get(i);
            //System.out.println(place.getName() + " " + place.getPlaceUrl());
        }
    }

    public void testTagsForPlace()
      throws FlickrException, IOException, SAXException {
        PlacesInterface placesInterface = flickr.getPlacesInterface();
        String placeId = null;
        Calendar minUploadDate = Calendar.getInstance();
        Calendar maxUploadDate = Calendar.getInstance();
        Calendar minTakenDate = Calendar.getInstance();
        Calendar maxTakenDate = Calendar.getInstance();
        minUploadDate.roll(Calendar.YEAR, -3);
        minTakenDate.roll(Calendar.YEAR, -3);
        ArrayList tags = placesInterface.tagsForPlace(
            sfWoeId, placeId,
            minUploadDate.getTime(), maxUploadDate.getTime(),
            minTakenDate.getTime(), maxTakenDate.getTime()
        );
        assertTrue((tags.size() > 0));
        boolean calFound = false;
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = (Tag) tags.get(i);
            if (tag.getValue().equals("california") &&
                tag.getCount() > 140000) {
                calFound = true;
            }
            //System.out.println(tag.getValue() + " " + tag.getCount());
        }
        assertTrue(calFound);
    }

    private void placeAssertions(Location location) {
        assertEquals(
            "kH8dLOubBZRvX_YZ",
            location.getPlaceId()
        );
        assertEquals(
            "/United+States/California/San+Francisco",
            location.getPlaceUrl()
        );
        assertEquals(
            "2487956",
            location.getWoeId()
        );
        assertEquals(
            37.779D,
            location.getLatitude()
        );
        assertEquals(
            -122.420D,
            location.getLongitude()
        );
        assertEquals(
            Place.TYPE_LOCALITY,
            location.getPlaceType()
        );

        assertEquals(
            "kH8dLOubBZRvX_YZ",
            location.getLocality().getPlaceId()
        );
        assertEquals(
            "San Francisco, California, United States",
            location.getLocality().getName()
        );
        assertEquals(
            "2487956",
            location.getLocality().getWoeId()
        );
        assertEquals(
            37.779D,
            location.getLocality().getLatitude()
        );
        assertEquals(
            -122.420D,
            location.getLocality().getLongitude()
        );

        assertEquals(
            "hCca8XSYA5nn0X1Sfw",
            location.getCounty().getPlaceId()
        );
        assertEquals(
            "San Francisco County, California, United States",
            location.getCounty().getName()
        );
        assertEquals(
            "12587707",
            location.getCounty().getWoeId()
        );
        assertEquals(
            37.759D,
            location.getCounty().getLatitude()
        );
        assertEquals(
            -122.435D,
            location.getCounty().getLongitude()
        );

        assertEquals(
            "SVrAMtCbAphCLAtP",
            location.getRegion().getPlaceId()
        );
        assertEquals(
            "California, United States",
            location.getRegion().getName()
        );
        assertEquals(
            "2347563",
            location.getRegion().getWoeId()
        );
        assertEquals(
            37.271D,
            location.getRegion().getLatitude()
        );
        assertEquals(
            -119.270D,
            location.getRegion().getLongitude()
        );

        assertEquals(
            "4KO02SibApitvSBieQ",
            location.getCountry().getPlaceId()
        );
        assertEquals(
            "United States",
            location.getCountry().getName()
        );
        assertEquals(
            "23424977",
            location.getCountry().getWoeId()
        );
        assertEquals(
            48.890D,
            location.getCountry().getLatitude()
        );
        assertEquals(
            -116.982D,
            location.getCountry().getLongitude()
        );
    }
}
