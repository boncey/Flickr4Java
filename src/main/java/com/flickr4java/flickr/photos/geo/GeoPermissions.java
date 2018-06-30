package com.flickr4java.flickr.photos.geo;

/**
 * Simple encapsulation of viewing permissions for geo data.
 * 
 * @author till (Till Krech - flickr:extranoise)
 * 
 */
public class GeoPermissions {
    private static final long serialVersionUID = 12L;

    private boolean pub;

    private boolean contact;

    private boolean friend;

    private boolean family;
    
    private String id;

    /**
     * @return true if contacts may see the geo data
     */
    public boolean isContact() {
        return contact;
    }

    /**
     * enables / disables viewing of geo data by contacs.
     * 
     * @param enable
     *            true enables / false diables
     */
    public void setContact(boolean enable) {
        this.contact = enable;
    }

    /**
     * @return true if "family members" may see the geo data
     */
    public boolean isFamily() {
        return family;
    }

    /**
     * enables / disables viewing of geo data by "family members".
     * 
     * @param enable
     *            true enables / false diables
     */
    public void setFamily(boolean enable) {
        this.family = enable;
    }

    /**
     * @return true if "friends" may see the geo data
     */
    public boolean isFriend() {
        return friend;
    }

    /**
     * enables / disables viewing of geo data by "friends".
     * 
     * @param enable
     *            true enables / false diables
     */
    public void setFriend(boolean enable) {
        this.friend = enable;
    }

    /**
     * @return true if anyone may see the geo data
     */
    public boolean isPublic() {
        return pub;
    }

    /**
     * enables / disables viewing of geo data by any one.
     * 
     * @param enable
     *            true enables / false diables
     */
    public void setPublic(boolean enable) {
        this.pub = enable;
    }
    
	/**
	 * @return the the photo_id
	 */
	public String getId() {
		return id;
	}

	/**
	* @param id the photo_id
	*/
	public void setId(String id) {
		this.id = id;
	}
}
