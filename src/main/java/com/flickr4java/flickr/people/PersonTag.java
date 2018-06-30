package com.flickr4java.flickr.people;

public class PersonTag {
	
	private String id;

	private String username;
	
	private String realName;
	
    private int iconFarm;

    private int iconServer;
    
    private String pathAlias;
	
	private String addedById;
	    
    private int x;
    
    private int y;
    
    private int w;
    
    private int h;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getIconFarm() {
		return iconFarm;
	}

	public void setIconFarm(int iconFarm) {
		this.iconFarm = iconFarm;
	}

	public int getIconServer() {
		return iconServer;
	}

	public void setIconServer(int iconServer) {
		this.iconServer = iconServer;
	}
	
	/**
	 * @return the user_id of the user who added the person
	 */
	public String getAddedById() {
		return addedById;
	}

	/**
	 * @param addedById the user_id who added the person
	 */
	public void setAddedById(String addedById) {
		this.addedById = addedById;
	}

	/**
	 * @return the x coordinate of the bounding box around the person
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set the coordinate of the bounding box around the person
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y coordinate of the bounding box around the person
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set the coordinate of the bounding box around the person
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the w coordinate of the bounding box around the person
	 */
	public int getW() {
		return w;
	}

	/**
	 * @param w the w to set the coordinate of the bounding box around the person
	 */
	public void setW(int w) {
		this.w = w;
	}

	/**
	 * @return the h coordinate of the bounding box around the person
	 */
	public int getH() {
		return h;
	}

	/**
	 * @param h the h to set the coordinate of the bounding box around the person
	 */
	public void setH(int h) {
		this.h = h;
	}

	public String getPathAlias() {
		return pathAlias;
	}

	public void setPathAlias(String pathAlias) {
		this.pathAlias = pathAlias;
	}

}
