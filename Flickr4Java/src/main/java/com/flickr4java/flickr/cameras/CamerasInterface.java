/**
 * @author acaplan
 */
package com.flickr4java.flickr.cameras;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for flickr.collections.* methods.
 * 
 * @author callmeal
 * @version $Id$ Copyright (c) 2012 allanc
 */
public class CamerasInterface {
    private static final String METHOD_GET_BRANDS = "flickr.cameras.getBrands";

    private static final String METHOD_GET_BRAND_MODELS = "flickr.cameras.getBrandModels";

    /**
     * Logger for log4j.
     */
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(CamerasInterface.class);

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public CamerasInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Returns all the brands of cameras that Flickr knows about.
     * 
     * This method does not require authentication.
     * 
     * 
     * @return List of Brands
     * @throws FlickrException
     */
    public List<Brand> getBrands() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_BRANDS);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        List<Brand> lst = new ArrayList<Brand>();
        Element mElement = response.getPayload();
        NodeList brandElements = mElement.getElementsByTagName("brand");
        for (int i = 0; i < brandElements.getLength(); i++) {
            Element brandElement = (Element) brandElements.item(i);
            Brand brand = new Brand();
            brand.setId(brandElement.getAttribute("id"));
            brand.setName(brandElement.getAttribute("name"));
            lst.add(brand);
        }

        return lst;
    }

    /**
     * Returns all the brands of cameras that Flickr knows about.
     * 
     * This method does not require authentication.
     * 
     * 
     * @return List of Brands
     * @throws FlickrException
     */
    public List<Camera> getBrandModels(String strBrand) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_BRAND_MODELS);
        parameters.put("brand", strBrand);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        List<Camera> lst = new ArrayList<Camera>();
        Element mElement = response.getPayload();
        NodeList cameraElements = mElement.getElementsByTagName("camera");
        for (int i = 0; i < cameraElements.getLength(); i++) {
            Element cameraElement = (Element) cameraElements.item(i);
            Camera cam = new Camera();
            cam.setId(cameraElement.getAttribute("id"));
            cam.setName(XMLUtilities.getChildValue(cameraElement, "name"));

            NodeList detailsNodes = cameraElement.getElementsByTagName("details");
            int n = detailsNodes.getLength();
            if (n == 1) {
                Element detailElement = (Element) detailsNodes.item(0);
                Details detail = new Details();
                cam.setDetails(detail);
                detail.setMegapixels(XMLUtilities.getChildValue(detailElement, "megapixels"));
                detail.setZoom(XMLUtilities.getChildValue(detailElement, "zoom"));
                detail.setLcdSize(XMLUtilities.getChildValue(detailElement, "lcd_screen_size"));
                detail.setStorageType(XMLUtilities.getChildValue(detailElement, "memory_type"));
            }

            NodeList imageNodes = cameraElement.getElementsByTagName("images");
            n = imageNodes.getLength();
            if (n == 1) {
                Element imageElement = (Element) imageNodes.item(0);
                cam.setSmallImage(XMLUtilities.getChildValue(imageElement, "small"));
                cam.setLargeImage(XMLUtilities.getChildValue(imageElement, "large"));
            }

            lst.add(cam);
        }

        return lst;
    }

}
