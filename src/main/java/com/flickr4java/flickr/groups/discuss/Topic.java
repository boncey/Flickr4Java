package com.flickr4java.flickr.groups.discuss;

public class Topic {

	private String topicId;
	private String subject;
	private String authorId;
	private String authorname;
	private String role;
	private int iconserver;
	private int iconfarm;
	private int countReplies;
	private boolean canEdit;
	private boolean canDelete;
	private boolean canReply;
	private boolean isSticky;
	private boolean isLocked;
	private String datecreate;
	private String datelastpost;
    private String message;
    private String lastReply = "";
    private boolean isPro;
    
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getIconserver() {
		return iconserver;
	}
	public void setIconserver(int iconserver) {
		this.iconserver = iconserver;
	}
	public int getIconfarm() {
		return iconfarm;
	}
	public void setIconfarm(int iconfarm) {
		this.iconfarm = iconfarm;
	}

	public boolean isCanEdit() {
		return canEdit;
	}
	public void setIsCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public boolean isCanDelete() {
		return canDelete;
	}
	public void setIsCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public boolean isCanReply() {
		return canReply;
	}
	public void setIsCanReply(boolean canReply) {
		this.canReply = canReply;
	}
	public boolean isSticky() {
		return isSticky;
	}
	public void setIsSticky(boolean isSticky) {
		this.isSticky = isSticky;
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public String getDatecreate() {
		return datecreate;
	}
	public void setDatecreate(String datecreate) {
		this.datecreate = datecreate;
	}
	public String getDatelastpost() {
		return datelastpost;
	}
	public void setDatelastpost(String datelastpost) {
		this.datelastpost = datelastpost;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCountReplies() {
		return countReplies;
	}
	public void setCountReplies(int countReplies) {
		this.countReplies = countReplies;
	}
	public String getLastReply() {
		return lastReply;
	}
	public void setLastReply(String lastReply) {
		this.lastReply = lastReply;
	}
	public boolean isPro() {
		return isPro;
	}
	public void setIsPro(boolean isPro) {
		this.isPro = isPro;
	}
	
}
