package com.flickr4java.flickr;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.flickr4java.flickr.util.XMLUtilities;

/**
 * Flickr SOAP Response object.
 * 
 * @author Matt Ray
 */
public class SOAPResponse implements Response {

    private List<Element> payload;

    private String errorCode;

    private String errorMessage;

    private SOAPEnvelope envelope;

    public SOAPResponse(SOAPEnvelope envelope) {
        this.envelope = envelope;
    }

    public void parse(Document document) {
        try {
            SOAPBody body = (SOAPBody) envelope.getBody();

            if (Flickr.debugStream) {
                System.out.println("SOAP RESPONSE.parse");
                System.out.println(body.getAsString());
            }

            SOAPFault fault = (SOAPFault) body.getFault();
            if (fault != null) {
                System.err.println("FAULT: " + fault.getAsString());
                errorCode = fault.getFaultCode();
                errorMessage = fault.getFaultString();
            } else {
                for (@SuppressWarnings("unchecked")
                Iterator<Element> i = body.getChildElements(); i.hasNext();) {
                    Element bodyelement = i.next();
                    bodyelement.normalize();
                    // TODO: Verify that the payload is always a single XML node
                    payload = (List<Element>) XMLUtilities.getChildElements(bodyelement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStat() {
        return null;
    }

    public Element getPayload() {
        if (payload.isEmpty()) {
            throw new RuntimeException("SOAP response payload has no elements");
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
