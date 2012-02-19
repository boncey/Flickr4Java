package com.flickr4java.flickr;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.Element;

import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.util.UrlUtilities;

/**
 * SOAP interface to flickr.
 * 
 * Not yet working with OAuth - please use {@link REST} instead.
 * 
 * @author Matt Ray
 */
public class SOAP extends Transport {

    private static final Logger logger = Logger.getLogger(SOAP.class);

    public static final String URN = "urn:flickr";

    public static final String BODYELEMENT = "FlickrRequest";

    public static final String PATH = "/services/soap/";

    // TODO [DJG] Configure service internally
    private OAuthService service;

    public SOAP() {
        setHost(Flickr.DEFAULT_HOST);
    }

    public SOAP(String host) {
        setHost(host);
    }

    @Deprecated
    // No need to expose OAuthService construction to outside world
    public SOAP(OAuthService service) {
        setTransportType(SOAP);
        setResponseClass(SOAPResponse.class);
        setPath(PATH);
        setHost(Flickr.DEFAULT_HOST);
        this.service = service;
    }

    /**
     * Invoke an HTTP GET request on a remote host. You must close the InputStream after you are done with.
     * 
     * @param path
     *            The request path
     * @param parameters
     *            The parameters (collection of Parameter objects)
     * @return The Response
     * @throws FlickrException
     */
    @Override
    public Response get(String path, Map<String, Object> parameters, String sharedSecret) throws FlickrException {
        // this is currently exactly the same as the post
        return post(path, parameters, sharedSecret);
    }

    /**
     * Invoke an HTTP POST request on a remote host.
     * 
     * @param path
     *            The request path
     * @param parameters
     *            The parameters (collection of Parameter objects)
     * @return The Response object
     * @throws FlickrException
     */
    @Override
    public Response post(String path, Map<String, Object> parameters, String sharedSecret, boolean multipart) throws FlickrException {

        OAuthRequest request = new OAuthRequest(Verb.POST, API_HOST + PATH);
        RequestContext requestContext = RequestContext.getRequestContext();
        Auth auth = requestContext.getAuth();
        Token requestToken = new Token(auth.getToken(), auth.getTokenSecret());
        service.signRequest(requestToken, request);

        try {
            URL url = UrlUtilities.buildUrl(getHost(), getPort(), path, Collections.<String, String> emptyMap());
            // build the envelope
            SOAPEnvelope env = new SOAPEnvelope();
            env.addNamespaceDeclaration("xsi", "http://www.w3.org/1999/XMLSchema-instance");
            env.addNamespaceDeclaration("xsd", "http://www.w3.org/1999/XMLSchema");

            // build the body
            Name bodyName = env.createName(BODYELEMENT, "x", URN);
            SOAPBodyElement body = new SOAPBodyElement(bodyName);

            // set the format to soap2
            Element e = XMLUtils.StringToElement("", "format", "soap2");
            SOAPElement sbe = new SOAPBodyElement(e);
            body.addChildElement(sbe);

            // add all the parameters to the body
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                e = XMLUtils.StringToElement("", entry.getKey(), String.valueOf(entry.getValue()));
                sbe = new SOAPBodyElement(e);
                body.addChildElement(sbe);
            }

            // put the body in the envelope
            env.addBodyElement(body);

            if (Flickr.debugStream) {
                logger.debug("SOAP ENVELOPE:");
                logger.debug(env.toString());
            }

            // build the call.
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(url);

            request.addPayload(env.getAsString());

            // SOAPEnvelope envelope = call.invoke(env);

            this.service.signRequest(requestToken, request);
            org.scribe.model.Response scribeResponse = request.send();

            if (Flickr.debugStream) {
                logger.debug("SOAP RESPONSE:");
                logger.debug(scribeResponse.getBody());
            }

            SOAPEnvelope envelope = new SOAPEnvelope(new ByteArrayInputStream(scribeResponse.getBody().getBytes("UTF-8")));
            SOAPResponse response = new SOAPResponse(envelope);
            response.parse(null); // the null is because we don't really need a document, but the Interface does
            return response;

        } catch (SOAPException se) {
            throw new FlickrRuntimeException(se);
        } catch (ServiceException se) {
            throw new FlickrRuntimeException(se);
        } catch (Exception se) {
            throw new FlickrRuntimeException(se);
        }
    }

    /**
     * Invoke a non OAuth HTTP GET request on a remote host.
     * 
     * This is only used for the Flickr OAuth methods checkToken and getAccessToken.
     * 
     * @param path
     *            The request path
     * @param parameters
     *            The parameters
     * @return The Response
     * @throws FlickrException
     */
    @Override
    public Response getNonOAuth(String path, Map<String, String> parameters) throws FlickrException {

        // TODO

        return null;
    }
}