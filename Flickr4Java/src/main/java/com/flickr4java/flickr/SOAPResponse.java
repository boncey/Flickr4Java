package com.flickr4java.flickr;

import com.flickr4java.flickr.util.XMLUtilities;

import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPFault;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Flickr SOAP Response object.
 * 
 * @author Matt Ray
 */
public class SOAPResponse implements Response {

    private static Logger _log = Logger.getLogger(SOAPResponse.class);

    private List<Element> payload;

    private String errorCode;

    private String errorMessage;

    private final SOAPEnvelope envelope;

    public SOAPResponse(SOAPEnvelope envelope) {
        this.envelope = envelope;
    }

    @Override
    public void parse(Document document) {
        try {
            SOAPBody body = (SOAPBody) envelope.getBody();

            if (Flickr.debugStream) {
                _log.debug("SOAP RESPONSE.parse");
                _log.debug(body.getAsString());
            }

            SOAPFault fault = (SOAPFault) body.getFault();
            if (fault != null) {
                _log.warn("FAULT: " + fault.getAsString());
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

    @Override
    public Element getPayload() {
        if (payload.isEmpty()) {
            throw new RuntimeException("SOAP response payload has no elements");
        }
        return payload.get(0);
    }

    @Override
    public Collection<Element> getPayloadCollection() {
        return payload;
    }

    @Override
    public boolean isError() {
        return errorCode != null;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
