package com.flickr4java.flickr.places;

/**
 * A Flickr place.<br>
 * As a result to {@link PlacesInterface#getInfo(String, String)} a Location describes the relation of a place to various types of other places.
 * 
 * @author mago
 * @version $Id: Location.java,v 1.4 2009/07/12 22:43:07 x-mago Exp $
 * @see com.flickr4java.flickr.photos.SearchParameters#setPlaceId(String)
 * @see com.flickr4java.flickr.photos.Photo#getPlaceId()
 */
public class Location {
    private static final long serialVersionUID = 12L;

    private String woeId = "";

    private String placeId = "";

    private String placeUrl = "";

    private Place locality = null;

    private int accuracy = 0;
    
    private Place county = null;

    private Place region = null;

    private Place country = null;

    private double latitude = 0.0;

    private double longitude = 0.0;

    private int placeType = 0;
    
    private String timezone;
    
    private String name;
    
    private String woeName;
    
    private boolean hasShapeData;
    
    private ShapeData shapeData;

    public Location() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public Place getLocality() {
        return locality;
    }

    public void setLocality(Place locality) {
        this.locality = locality;
    }

    public Place getCounty() {
        return county;
    }

    public void setCounty(Place county) {
        this.county = county;
    }

    public Place getRegion() {
        return region;
    }

    public void setRegion(Place region) {
        this.region = region;
    }

    public Place getCountry() {
        return country;
    }

    public void setCountry(Place country) {
        this.country = country;
    }

    /**
     * 
     * @return woeid
     * @see com.flickr4java.flickr.photos.SearchParameters#setWoeId(String)
     */
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

    public int getPlaceType() {
        return placeType;
    }

    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWoeName() {
		return woeName;
	}

	public void setWoeName(String woeName) {
		this.woeName = woeName;
	}

	public boolean isHasShapeData() {
		return hasShapeData;
	}

	public void setIsHasShapeData(boolean hasShapeData) {
		this.hasShapeData = hasShapeData;
	}

	public ShapeData getShapedata() {
		return shapeData;
	}

	public void setShapedata(ShapeData shapeData) {
		this.shapeData = shapeData;
	}



}
