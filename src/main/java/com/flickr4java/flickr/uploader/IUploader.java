package com.flickr4java.flickr.uploader;

import com.flickr4java.flickr.FlickrException;

import java.io.File;
import java.io.InputStream;

public interface IUploader {
    String upload(byte[] data, UploadMetaData metaData) throws FlickrException;

    String upload(File file, UploadMetaData metaData) throws FlickrException;

    String upload(InputStream in, UploadMetaData metaData) throws FlickrException;

    String replace(InputStream in, String flickrId, boolean async) throws FlickrException;

    String replace(byte[] data, String flickrId, boolean async) throws FlickrException;

    String replace(File file, String flickrId, boolean async) throws FlickrException;
}
