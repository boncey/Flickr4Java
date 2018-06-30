package com.flickr4java.flickr.places;

/**
 * Describes a place inside a {@link Location}.
 * 
 * Each place contain its place ID, corresponding URL (underneath <a href="http://www.flickr.com/places/" target="_top">http://www.flickr.com/places/</a>) and
 * place type for disambiguating different locations with the same name.
 * <p>
 * 
 * A place delivered by find contains an URL, whereas the URL is missing if delivered by resolvePlaceId and resolvePlaceUrl.
 * 
 * @author mago
 * @version $Id: Place.java,v 1.7 2009/07/12 22:43:07 x-mago Exp $
 */
public class Place {
    private static final long serialVersionUID = 12L;

    public static final int TYPE_UNSET = 0;

    public static final int TYPE_LOCALITY = 7;

    public static final int TYPE_COUNTY = 9;

    public static final int TYPE_REGION = 8;

    public static final int TYPE_COUNTRY = 12;

    public static final int TYPE_CONTINENT = 29;

    public static final int TYPE_NEIGHBOURHOOD = 22;

    private String name = "";

    private String placeId = "";

    private String woeId = "";

    private double latitude = 0.0;

    private double longitude = 0.0;

    /**
     * Set only if requested by find.
     */
    private String placeUrl = "";

    private int placeType = 0;

    private int photoCount = 0;

    public Place() {
    }

    public Place(String placeId, String name) {
        this.name = name;
        this.placeId = placeId;
    }

    public Place(String placeId, String name, int placeType) {
        this.name = name;
        this.placeId = placeId;
        this.placeType = placeType;
    }

    public Place(String placeId, String name, String woeId) {
        this.name = name;
        this.placeId = placeId;
        this.woeId = woeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * The PlaceType.
     * 
     * @return The PlaceType
     * @see Place#TYPE_COUNTRY
     * @see Place#TYPE_COUNTY
     * @see Place#TYPE_LOCALITY
     * @see Place#TYPE_REGION
     */
    public int getPlaceType() {
        return placeType;
    }

    /**
     * The PlaceType.
     * 
     * @param placeType
     * @see Place#TYPE_COUNTRY
     * @see Place#TYPE_COUNTY
     * @see Place#TYPE_LOCALITY
     * @see Place#TYPE_REGION
     * @see Place#TYPE_NEIGHBOURHOOD
     * @see Place#TYPE_CONTINENT
     */
    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

    public void setPlaceType(String placeType) {
        try {
            setPlaceType(Integer.parseInt(placeType));
        } catch (NumberFormatException e) {
        }
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public String getWoeId() {
        return woeId;
    }

    public void setWoeId(String woeId) {
        this.woeId = woeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        try {
            setLatitude(Double.parseDouble(latitude));
        } catch (NumberFormatException e) {
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        try {
            setLongitude(Double.parseDouble(longitude));
        } catch (NumberFormatException e) {
        }
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(String photoCount) {
        try {
            setPhotoCount(Integer.parseInt(photoCount));
        } catch (NumberFormatException e) {
        }
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, placeId);
    }
}
