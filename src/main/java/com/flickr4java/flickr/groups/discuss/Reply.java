package com.flickr4java.flickr.groups.discuss;

public class Reply {

	private String replyId;
	private String authorId;
	private String authorname;
	private String role;
	private int iconserver;
	private int iconfarm;
	private boolean canEdit;
	private boolean canDelete;
	private String datecreate;
    private String message;
    private String lastEdit;
    private boolean isPro;
    
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
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
	public int getIconserver() {
		return iconserver;
	}
	public void setIconserver(int iconserver) {
		this.iconserver = iconserver;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
	public String getDatecreate() {
		return datecreate;
	}
	public void setDatecreate(String datecreate) {
		this.datecreate = datecreate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(String lastEdit) {
		this.lastEdit = lastEdit;
	}
	public boolean isPro() {
		return isPro;
	}
	public void setIsPro(boolean isPro) {
		this.isPro = isPro;
	}
}
