package com.flickr4java.flickr.photos;

/**
 * Represents either a photo pool or a photo set with it's id and title. Instances of this class are distinguished as set or pool by their <em>kind</em>
 * property. There are two possible kinds represented as public int constants:
 * <ul>
 * <li>SET this is a photo set</li>
 * <li>POOL this is a groups photo pool</li>
 * </ul>
 * 
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: PhotoPlace.java,v 1.3 2009/07/12 22:43:07 x-mago Exp $
 */
public class PhotoPlace {
    public static final int SET = 1;

    public static final int POOL = 2;

    private String id;

    private int kind;

    private String title;

    /**
     * creates a new one.
     * 
     * @param kind
     *            either SET or POOL
     * @param id
     *            id of the pool or set
     * @param title
     *            name of the pool or set
     * @throws IllegalArgumentException
     *             if kind is invalid
     */
    public PhotoPlace(int kind, String id, String title) {
        setKind(kind);
        this.id = id;
        this.title = title;
    }

    /**
     * creates a new one where the kind may be specified as String "set" or "pool"
     * 
     * @param kind
     *            either "set" or "pool"
     * @param id
     *            id of the pool or set
     * @param title
     *            name of the pool or set
     * @throws IllegalArgumentException
     *             if kind is invalid
     */
    public PhotoPlace(String kind, String id, String title) {
        setKind(kind);
        this.id = id;
        this.title = title;
    }

    /**
     * @return the pool or set id
     */
    public String getId() {
        return id;
    }

    /**
     * @return what I am
     */
    public int getKind() {
        return kind;
    }

    protected void setKind(int kind) {
        this.kind = kind;
    }

    protected void setKind(String kindStr) {
        if ("pool".equalsIgnoreCase(kindStr)) {
            setKind(POOL);
        } else if ("set".equalsIgnoreCase(kindStr)) {
            setKind(SET);
        } else {
            throw new IllegalArgumentException("Invalid kind [" + kindStr + "]");
        }
    }

    /**
     * @return the name of the pool or set
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return what I am as String
     */
    public String getKindAsString() {
        String s;
        switch (kind) {
        case SET:
            s = "set";
            break;
        case POOL:
            s = "pool";
            break;
        default:
            s = "unknown(" + kind + ")";
            break;
        }
        return s;
    }

    /**
     * compares this to another object. Makes is possible to put PhotPlaces into a Java Set.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PhotoPlace)) {
            return false;
        }
        PhotoPlace other = (PhotoPlace) obj;
        if (other.kind != kind) {
            return false;
        }
        if (this.id != null) {
            if (!this.id.equals(other.id)) {
                return false;
            }
        } else if (other.id != null) {
            return false;
        }
        if (this.title != null) {
            if (!this.title.equals(other.title)) {
                return false;
            }
        } else if (other.title != null) {
            return false;
        }
        return true;
    }

    /**
     * primitive hashCode. Makes is possible to put PhotPlaces into a Java Set.
     */
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * returns a human readable but ugly representation of this object
     */
    public String toString() {
        return getClass().getName() + "[" + getKindAsString() + " id=\"" + id + "\" title=\"" + title + "\"]";
    }

}
