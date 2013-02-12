/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.photos;

import java.util.Arrays;
import java.util.List;

/**
 * This class descibes a Size of a Photo.
 * <p>
 * 
 * @author Anthony Eden
 * @version $Id: Size.java,v 1.7 2009/07/23 20:41:03 x-mago Exp $
 * 
 *          TODO This could probably be changed to be an enum
 */
public class Size {

    /**
     * Thumbnail, 100 on longest side.
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int THUMB = 0;

    /**
     * Small square 75x75.
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SQUARE = 1;

    /**
     * Small, 240 on longest side.
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SMALL = 2;

    /**
     * Medium, 500 on longest side.
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int MEDIUM = 3;

    /**
     * Large, 1024 on longest side (only exists for very large original images).
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int LARGE = 4;

    /**
     * Original image, either a jpg, gif or png, depending on source format.<br>
     * Only from pro-users original images are available!
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int ORIGINAL = 5;

    /**
     * Large Square 150x150
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SQUARE_LARGE = 6;

    /**
     * Small, 320 px on the longest side
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SMALL_320 = 7;

    /**
     * Medium, 640 px on the longest side
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int MEDIUM_640 = 8;

    /**
     * Medium, 640 px on the longest side
     * 
     * @see com.flickr4java.flickr.photos.Size#getLabel()
     * @see com.flickr4java.flickr.photos.Size#setLabel(int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImage(Photo, int)
     * @see com.flickr4java.flickr.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int MEDIUM_800 = 9;

    private int label;

    private int width;

    private int height;

    private String source;

    private String url;

    public Size() {

    }

    /**
     * Size of the Photo.
     * 
     * @return label
     * @see com.flickr4java.flickr.photos.Size#THUMB
     * @see com.flickr4java.flickr.photos.Size#SQUARE
     * @see com.flickr4java.flickr.photos.Size#SMALL
     * @see com.flickr4java.flickr.photos.Size#MEDIUM
     * @see com.flickr4java.flickr.photos.Size#LARGE
     * @see com.flickr4java.flickr.photos.Size#ORIGINAL
     * @see com.flickr4java.flickr.photos.Size#SQUARE_LARGE
     * @see com.flickr4java.flickr.photos.Size#SMALL_320
     * @see com.flickr4java.flickr.photos.Size#MEDIUM_640
     * @see com.flickr4java.flickr.photos.Size#MEDIUM_800
     */
    public int getLabel() {
        return label;
    }

    private final List<String> lstSizes = Arrays.asList("Thumbnail", "Square", "Small", "Medium", "Large", "Original", "Square Large", "Small 320",
            "Medium 640", "Medium 800");

    /**
     * Set the String-representation of size.
     * 
     * Like: Square, Thumbnail, Small, Medium, Large, Original.
     * 
     * @param label
     */
    public void setLabel(String label) {
        int ix = lstSizes.indexOf(label);
        if (ix != -1) {
            setLabel(ix);
        }
    }

    /**
     * Size of the Photo.
     * 
     * @param label
     *            The integer-representation of a size
     * @see com.flickr4java.flickr.photos.Size#THUMB
     * @see com.flickr4java.flickr.photos.Size#SQUARE
     * @see com.flickr4java.flickr.photos.Size#SMALL
     * @see com.flickr4java.flickr.photos.Size#MEDIUM
     * @see com.flickr4java.flickr.photos.Size#LARGE
     * @see com.flickr4java.flickr.photos.Size#ORIGINAL
     */
    public void setLabel(int label) {
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setWidth(String width) {
        if (width != null) {
            setWidth(Integer.parseInt(width));
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHeight(String height) {
        if (height != null) {
            setHeight(Integer.parseInt(height));
        }
    }

    /**
     * URL of the image.
     * 
     * @return Image-URL
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * URL of the photopage.
     * 
     * @return Page-URL
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Size test = (Size) obj;
        return label == test.label && width == test.width && height == test.height && areEqual(source, test.source) && areEqual(url, test.url);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash += new Integer(label).hashCode();
        hash += new Integer(width).hashCode();
        hash += new Integer(height).hashCode();
        if (source != null) {
            hash += source.hashCode();
        }
        if (url != null) {
            hash += url.hashCode();
        }
        return hash;
    }

    private boolean areEqual(Object x, Object y) {
        return x == null ? y == null : x.equals(y);
    }
}
