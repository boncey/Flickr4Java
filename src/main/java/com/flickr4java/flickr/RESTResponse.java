
package com.flickr4java.flickr;

import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.List;

/**
 * Flickr Response object.
 * 
 * @author Anthony Eden
 */
public class RESTResponse implements Response {

    private String stat;

    private List<Element> payload;

    private String errorCode;

    private String errorMessage;

    public void parse(Document document) {
        Element rspElement = document.getDocumentElement();
        rspElement.normalize();
        stat = rspElement.getAttribute("stat");
        if ("ok".equals(stat)) {
            // TODO: Verify that the payload is always a single XML node
            payload = (List<Element>) XMLUtilities.getChildElements(rspElement);
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
        if (payload.isEmpty()) {
            throw new RuntimeException("REST response payload has no elements");
        }
        return payload.get(0);
    }

    public Collection<Element> getPayloadCollection() {
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
