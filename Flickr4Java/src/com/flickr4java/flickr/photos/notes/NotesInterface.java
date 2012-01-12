/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.photos.notes;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.photos.Note;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Anthony Eden
 */
public class NotesInterface {

    public static final String METHOD_ADD = "flickr.photos.notes.add";
    public static final String METHOD_DELETE = "flickr.photos.notes.delete";
    public static final String METHOD_EDIT = "flickr.photos.notes.edit";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public NotesInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Add a note to a photo.  The Note object bounds and text must be specified.
     *
     * @param photoId The photo ID
     * @param note The Note object
     * @return The updated Note object
     */
    public Note add(String photoId, Note note) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_ADD));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        Rectangle bounds = note.getBounds();
        if (bounds != null) {
            parameters.add(new Parameter("note_x", String.valueOf(bounds.x)));
            parameters.add(new Parameter("note_y", String.valueOf(bounds.y)));
            parameters.add(new Parameter("note_w", String.valueOf(bounds.width)));
            parameters.add(new Parameter("note_h", String.valueOf(bounds.height)));
        }
        String text = note.getText();
        if (text != null) {
            parameters.add(new Parameter("note_text", text));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element noteElement = response.getPayload();
        note.setId(noteElement.getAttribute("id"));
        return note;
    }

    /**
     * Delete the specified note.
     *
     * @param noteId The node ID
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void delete(String noteId) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_DELETE));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("note_id", noteId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Update a note.
     *
     * @param note The Note to update
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public void edit(Note note) throws IOException, SAXException, FlickrException {
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_EDIT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("note_id", note.getId()));
        Rectangle bounds = note.getBounds();
        if (bounds != null) {
            parameters.add(new Parameter("note_x", String.valueOf(bounds.x)));
            parameters.add(new Parameter("note_y", String.valueOf(bounds.y)));
            parameters.add(new Parameter("note_w", String.valueOf(bounds.width)));
            parameters.add(new Parameter("note_h", String.valueOf(bounds.height)));
        }
        String text = note.getText();
        if (text != null) {
            parameters.add(new Parameter("note_text", text));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.post(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
