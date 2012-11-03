package com.flickr4java.flickr.photos;

import com.flickr4java.flickr.util.StringUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

/**
 * A geographic position.
 * 
 * @author mago
 * @version $Id: GeoData.java,v 1.4 2009/07/23 20:41:03 x-mago Exp $
 */
public class GeoData {

    private float longitude;

    private float latitude;

    private int accuracy;

    public GeoData() {
        super();
    }

    public GeoData(String longitudeStr, String latitudeStr, String accuracyStr) {
        longitude = Float.parseFloat(longitudeStr);
        latitude = Float.parseFloat(latitudeStr);
        accuracy = Integer.parseInt(accuracyStr);
    }

    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Set the accuracy level.
     * <p>
     * 
     * World level is 1, Country is ~3, Region ~6, City ~11, Street ~16.
     * 
     * @param accuracy
     * @see com.flickr4java.flickr.Flickr#ACCURACY_WORLD
     * @see com.flickr4java.flickr.Flickr#ACCURACY_COUNTRY
     * @see com.flickr4java.flickr.Flickr#ACCURACY_REGION
     * @see com.flickr4java.flickr.Flickr#ACCURACY_CITY
     * @see com.flickr4java.flickr.Flickr#ACCURACY_STREET
     */
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return "GeoData[longitude=" + longitude + " latitude=" + latitude + " accuracy=" + accuracy + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        // object must be GeoData at this point
        GeoData test = (GeoData) obj;
        Class cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                try {
                    Object res = method[i].invoke(this);
                    Object resTest = method[i].invoke(test);
                    String retType = method[i].getReturnType().toString();
                    if (retType.indexOf("class") == 0) {
                        if (res != null && resTest != null) {
                            if (!res.equals(resTest))
                                return false;
                        } else {
                            // return false;
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer) resTest)))
                            return false;
                    } else if (retType.equals("float")) {
                        if (!((Float) res).equals(((Float) resTest)))
                            return false;
                    } else {
                        System.out.println(method[i].getName() + "|" + method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("GeoData equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    // System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("GeoData equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash += new Float(longitude).hashCode();
        hash += new Float(latitude).hashCode();
        hash += new Integer(accuracy).hashCode();
        return hash;
    }
}
