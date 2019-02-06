

package com.flickr4java.flickr.uploader;

import com.flickr4java.flickr.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.Collection;

/**
 * Parsing the response after an upload.
 * 
 * @author Anthony Eden
 * @version $Id: UploaderResponse.java,v 1.7 2007/11/02 21:46:52 x-mago Exp $
 */
public class UploaderResponse implements Response {

    private String status;

    private String photoId;

    private String ticketId;

    private String errorCode;

    private String errorMessage;

    private Element responsePayLoad;

    /**
     * Parsing the response.
     * <p>
     * After a successful sychronous upload the photId is set.<br>
     * After an asychronous upload the ticketId.
     * 
     * @see #getPhotoId()
     * @see #getTicketId()
     */
    public void parse(Document document) {
        responsePayLoad = document.getDocumentElement();
        status = responsePayLoad.getAttribute("stat");
        if ("ok".equals(status)) {
            Element photoIdElement = (Element) responsePayLoad.getElementsByTagName("photoid").item(0);
            if (photoIdElement != null) {
                photoId = ((Text) photoIdElement.getFirstChild()).getData();
            } else {
                photoId = null;
            }
            Element ticketIdElement = (Element) responsePayLoad.getElementsByTagName("ticketid").item(0);
            if (ticketIdElement != null) {
                ticketId = ((Text) ticketIdElement.getFirstChild()).getData();
            } else {
                ticketId = null;
            }
        } else {
            Element errElement = (Element) responsePayLoad.getElementsByTagName("err").item(0);
            errorCode = errElement.getAttribute("code");
            errorMessage = errElement.getAttribute("msg");
        }
    }

    public String getStatus() {
        return status;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public boolean isError() {
        return errorMessage != null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @see com.flickr4java.flickr.Response#getPayload()
     */
    public Element getPayload() {
        return responsePayLoad;
    }

    /**
     * @see com.flickr4java.flickr.Response#getPayloadCollection()
     */
    public Collection<Element> getPayloadCollection() {
        return null;
    }

}
