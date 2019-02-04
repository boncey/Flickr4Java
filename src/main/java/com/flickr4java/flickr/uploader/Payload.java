package com.flickr4java.flickr.uploader;

import com.flickr4java.flickr.FlickrRuntimeException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Payload {

    private byte[] payload;

    private String photoId;

    public Payload(byte[] payload) {
        this.payload = payload;
    }

    public Payload(File file) {
        try {
            payload = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new FlickrRuntimeException(e);
        }
    }

    public Payload(InputStream inputStream) {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            payload = buffer.toByteArray();
        } catch (IOException e) {
            throw new FlickrRuntimeException(e);
        }
    }

    public Payload(InputStream inputStream, String photoId) {
        this(inputStream);
        this.photoId = photoId;
    }

    public Payload(byte[] payload, String photoId) {
        this(payload);
        this.photoId = photoId;
    }

    public Payload(File file, String photoId) {
        this(file);
        this.photoId = photoId;
    }

    public byte[] getPayload() {
        return payload;
    }

    public String getPhotoId() {
        return photoId;
    }
}
