
package com.flickr4java.flickr.reflection;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for testing the complete implementation of all Flickr-methods.
 * <p>
 * 
 * @author Anthony Eden
 * @version $Id: ReflectionInterface.java,v 1.10 2008/01/28 23:01:45 x-mago Exp $
 */
public class ReflectionInterface {

    private static Logger _log = LoggerFactory.getLogger(ReflectionInterface.class);

    public static final String METHOD_GET_METHOD_INFO = "flickr.reflection.getMethodInfo";

    public static final String METHOD_GET_METHODS = "flickr.reflection.getMethods";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transport;

    /**
     * Construct a ReflectionInterface.
     * 
     * @param apiKey
     *            The API key
     * @param sharedSecret
     *            The Shared Secret
     * @param transport
     *            The Transport interface
     */
    public ReflectionInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the info for the specified method.
     * 
     * @param methodName
     *            The method name
     * @return The Method object
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Method getMethodInfo(String methodName) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_METHOD_INFO);

        parameters.put("method_name", methodName);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element methodElement = response.getPayload();
        Method method = new Method();
        method.setName(methodElement.getAttribute("name"));
        method.setNeedsLogin("1".equals(methodElement.getAttribute("needslogin")));
        method.setNeedsSigning("1".equals(methodElement.getAttribute("needssigning")));
        String requiredPermsStr = methodElement.getAttribute("requiredperms");
        if (requiredPermsStr != null && requiredPermsStr.length() > 0) {
            try {
                int perms = Integer.parseInt(requiredPermsStr);
                method.setRequiredPerms(perms);
            } catch (NumberFormatException e) {
                // what shall we do?
                e.printStackTrace();
            }
        }
        method.setDescription(XMLUtilities.getChildValue(methodElement, "description"));
        method.setResponse(XMLUtilities.getChildValue(methodElement, "response"));
        method.setExplanation(XMLUtilities.getChildValue(methodElement, "explanation"));

        List<Argument> arguments = new ArrayList<Argument>();
        Element argumentsElement = XMLUtilities.getChild(methodElement, "arguments");
        // tolerant fix for incorrect nesting of the <arguments> element
        // as observed in current flickr responses of this method
        //
        // specified as
        // <rsp>
        // <method>
        // <arguments>
        // <errors>
        // <method>
        // </rsp>
        //
        // observed as
        // <rsp>
        // <method>
        // <arguments>
        // <errors>
        // </rsp>
        //
        if (argumentsElement == null) {
            _log.debug("getMethodInfo: Using workaround for arguments array");
            Element parent = (Element) methodElement.getParentNode();
            Element child = XMLUtilities.getChild(parent, "arguments");
            if (child != null) {
                argumentsElement = child;
            }
        }
        NodeList argumentElements = argumentsElement.getElementsByTagName("argument");
        for (int i = 0; i < argumentElements.getLength(); i++) {
            Argument argument = new Argument();
            Element argumentElement = (Element) argumentElements.item(i);
            argument.setName(argumentElement.getAttribute("name"));
            argument.setOptional("1".equals(argumentElement.getAttribute("optional")));
            argument.setDescription(XMLUtilities.getValue(argumentElement));
            arguments.add(argument);
        }
        method.setArguments(arguments);

        Element errorsElement = XMLUtilities.getChild(methodElement, "errors");
        // tolerant fix for incorrect nesting of the <errors> element
        // as observed in current flickr responses of this method
        // as of 2006-09-15
        //
        // specified as
        // <rsp>
        // <method>
        // <arguments>
        // <errors>
        // <method>
        // </rsp>
        //
        // observed as
        // <rsp>
        // <method>
        // <arguments>
        // <errors>
        // </rsp>
        //
        if (errorsElement == null) {
            _log.debug("getMethodInfo: Using workaround for errors array");
            Element parent = (Element) methodElement.getParentNode();
            Element child = XMLUtilities.getChild(parent, "errors");
            if (child != null) {
                errorsElement = child;
            }
        }
        List<Error> errors = new ArrayList<Error>();
        NodeList errorElements = errorsElement.getElementsByTagName("error");
        for (int i = 0; i < errorElements.getLength(); i++) {
            Error error = new Error();
            Element errorElement = (Element) errorElements.item(i);
            error.setCode(errorElement.getAttribute("code"));
            error.setMessage(errorElement.getAttribute("message"));
            error.setExplaination(XMLUtilities.getValue(errorElement));
            errors.add(error);
        }
        method.setErrors(errors);

        return method;
    }

    /**
     * Get a list of all methods.
     * 
     * @return The method names
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public Collection<String> getMethods() throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_METHODS);

        Response response = transport.get(transport.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element methodsElement = response.getPayload();

        List<String> methods = new ArrayList<String>();
        NodeList methodElements = methodsElement.getElementsByTagName("method");
        for (int i = 0; i < methodElements.getLength(); i++) {
            Element methodElement = (Element) methodElements.item(i);
            methods.add(XMLUtilities.getValue(methodElement));
        }
        return methods;
    }

}
