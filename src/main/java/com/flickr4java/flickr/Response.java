

package com.flickr4java.flickr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;

/**
 * @author Anthony Eden
 */
public interface Response {

    void parse(Document document);

    boolean isError();

    String getErrorCode();

    String getErrorMessage();

    Element getPayload();

    Collection<Element> getPayloadCollection();
}
