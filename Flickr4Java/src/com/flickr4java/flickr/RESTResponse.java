/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import java.util.Collection;
import java.util.Iterator;

import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Flickr Response object.
 *
 * @author Anthony Eden
 */
public class RESTResponse implements Response {

    private String stat;
    private Collection payload;

    private String errorCode;
    private String errorMessage;

    public void parse(Document document) {
        Element rspElement = document.getDocumentElement();
        rspElement.normalize();
        stat = rspElement.getAttribute("stat");
        if ("ok".equals(stat)) {
            // TODO: Verify that the payload is always a single XML node
            payload = XMLUtilities.getChildElements(rspElement);
        } else if ("fail".equals(stat)) {
            Element errElement = (Element) rspElement.getElementsByTagName("err").item(0);
            errorCode = errElement.getAttribute("code");
            errorMessage = errElement.getAttribute("msg");
        }
    }

    public String getStat() {
        return stat;
    }

    public Element getPayload() {
        Iterator iter = payload.iterator();
        if (iter.hasNext()) {
            return (Element) iter.next();
        } else {
            throw new RuntimeException("REST response payload has no elements");
        }
    }

    public Collection getPayloadCollection() {
        return payload;
    }

    public boolean isError() {
        return errorCode != null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }



}
