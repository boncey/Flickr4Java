package com.flickr4java.flickr.commons;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author mago
 * @version $Id: CommonsInterface.java,v 1.2 2009/07/11 20:30:27 x-mago Exp $
 */
public class CommonsInterface {
    public static final String METHOD_GET_INSTITUTIONS = "flickr.commons.getInstitutions";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public CommonsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Retrieves a list of the current Commons institutions.
     * 
     * This method does not require authentication.
     * 
     * @return List of Institution
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public ArrayList<Institution> getInstitutions() throws FlickrException {
        ArrayList<Institution> institutions = new ArrayList<Institution>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INSTITUTIONS);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element mElement = response.getPayload();

        NodeList mNodes = mElement.getElementsByTagName("institution");
        for (int i = 0; i < mNodes.getLength(); i++) {
            Element element = (Element) mNodes.item(i);
            institutions.add(parseInstitution(element));
        }
        return institutions;
    }

    private Institution parseInstitution(Element mElement) {
        Institution inst = new Institution();
        inst.setId(mElement.getAttribute("nsid"));
        inst.setDateLaunch(mElement.getAttribute("date_launch"));
        inst.setName(XMLUtilities.getChildValue(mElement, "name"));
        NodeList urlNodes = mElement.getElementsByTagName("url");
        for (int i = 0; i < urlNodes.getLength(); i++) {
            Element urlElement = (Element) urlNodes.item(i);
            if (urlElement.getAttribute("type").equals("site")) {
                inst.setSiteUrl(XMLUtilities.getValue(urlElement));
            } else if (urlElement.getAttribute("type").equals("license")) {
                inst.setLicenseUrl(XMLUtilities.getValue(urlElement));
            } else if (urlElement.getAttribute("type").equals("flickr")) {
                inst.setFlickrUrl(XMLUtilities.getValue(urlElement));
            }
        }
        return inst;
    }
}
