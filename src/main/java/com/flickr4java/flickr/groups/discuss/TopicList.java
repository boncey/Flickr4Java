package com.flickr4java.flickr.groups.discuss;

import com.flickr4java.flickr.SearchResultList;

public class TopicList<E> extends  SearchResultList<Topic> {
    private static final long serialVersionUID = 617037681128L;
    private String groupId;
    private int iconServer;
    private int iconFarm;
    private String name;
    private int members;
    private int privacy;
    private String language;
    private boolean isPoolModerated;
    
	public int getIconServer() {
		return iconServer;
	}
	public void setIconServer(int iconServer) {
		this.iconServer = iconServer;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public int getIconFarm() {
		return iconFarm;
	}
	public void setIconFarm(int iconFarm) {
		this.iconFarm = iconFarm;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	public int getPrivacy() {
		return privacy;
	}
	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public boolean isPoolModerated() {
		return isPoolModerated;
	}
	public void setIsPoolModerated(boolean isPoolModerated) {
		this.isPoolModerated = isPoolModerated;
	}

}
