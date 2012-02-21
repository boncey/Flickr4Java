/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr;

import java.util.Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
