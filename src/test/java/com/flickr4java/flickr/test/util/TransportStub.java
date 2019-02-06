package com.flickr4java.flickr.test.util;

import com.flickr4java.flickr.*;
import com.flickr4java.flickr.uploader.Payload;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.UploaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TransportStub extends Transport {

    private static final Logger _log = LoggerFactory.getLogger(TransportStub.class);

    private final DocumentBuilder builder;

    public TransportStub() {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new FlickrRuntimeException(e);
        }
    }

    @Override
    public Response get(String path, Map<String, Object> parameters, String apiKey, String sharedSecret) {
        return loadResponseFromFile(parameters.get("method"), "get");
    }

    @Override
    public Response post(String path, Map<String, Object> parameters, String apiKey, String sharedSecret) {
        return loadResponseFromFile(parameters.get("method"), "post");
    }

    @Override
    public Response postMultiPart(String path, UploadMetaData parameters, Payload payload, String apiKey, String sharedSecret) {
        return loadMulitpartResponseFromFile("upload", "post");
    }

    @Override
    public Response getNonOAuth(String path, Map<String, String> parameters) {
        return null;
    }

    private Response loadResponseFromFile(Object flickrMethod, String httpMethod) {
        RESTResponse response;
        String filename = String.format("/payloads/%s/%s.xml", httpMethod, flickrMethod);
        try {
            Path filePath = Paths.get(this.getClass().getResource(filename).toURI());
            String strXml = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            Document document = builder.parse(new InputSource(new StringReader(strXml)));

            response = new RESTResponse();
            response.parse(document);
        } catch (Exception e) {
            throw new FlickrRuntimeException(e);
        }

        return response;
    }

    private Response loadMulitpartResponseFromFile(Object flickrMethod, String httpMethod) {
        UploaderResponse response;
        String filename = String.format("/payloads/%s/%s.xml", httpMethod, flickrMethod);
        try {
            Path filePath = Paths.get(this.getClass().getResource(filename).toURI());
            String strXml = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            Document document = builder.parse(new InputSource(new StringReader(strXml)));

            response = new UploaderResponse();
            response.parse(document);
        } catch (Exception e) {
            throw new FlickrRuntimeException(e);
        }

        return response;
    }
}
