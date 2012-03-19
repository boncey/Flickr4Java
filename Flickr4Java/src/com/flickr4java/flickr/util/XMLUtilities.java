/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Anthony Eden
 * @version $Id: XMLUtilities.java,v 1.9 2009/03/04 18:46:58 x-mago Exp $
 */
public class XMLUtilities {

    private XMLUtilities() {
    }

    public static Collection<Element> getChildElements(Node node) {
        List<Element> elements = new ArrayList<Element>();
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode instanceof Element) {
                elements.add((Element) childNode);
            }
        }
        return elements;
    }

    /**
     * Get the text value for the specified element. If the element is null, or the element's body is empty then this method will return null.
     * 
     * @param element
     *            The Element
     * @return The value String or null
     */
    public static String getValue(Element element) {
        if (element != null) {
            Node dataNode = element.getFirstChild();
            if (dataNode != null) {
                return ((Text) dataNode).getData();
            }
        }
        return null;
    }

    /**
     * Get the first child element with the given name.
     * 
     * @param element
     *            The parent element
     * @param name
     *            The child element name
     * @return The child element or null
     */
    public static Element getChild(Element element, String name) {
        return (Element) element.getElementsByTagName(name).item(0);
    }

    /**
     * Get the value of the fist child element with the given name.
     * 
     * @param element
     *            The parent element
     * @param name
     *            The child element name
     * @return The child element value or null
     */
    public static String getChildValue(Element element, String name) {
        return getValue(getChild(element, name));
    }

    public static int getIntAttribute(Element el, String name) {
        String s = el.getAttribute(name);
        if (s != null && s.length() > 0) {
            return Integer.parseInt(s);
        }
        return 0;
    }

    public static boolean getBooleanAttribute(Element el, String name) {
        String s = el.getAttribute(name);
        if (s == null || "0".equals(s)) {
            return false;
        }
        if ("1".equals(s)) {
            return true;
        }
        return Boolean.getBoolean(s);
    }

}
