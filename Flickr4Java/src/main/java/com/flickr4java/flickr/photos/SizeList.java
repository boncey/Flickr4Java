package com.flickr4java.flickr.photos;

import java.util.ArrayList;



public class SizeList<E> extends ArrayList<Size> {
	
	/**
	 * @author Jonathan Willis
	 */
	private static final long serialVersionUID = -4735611134085303463L;
	
	private boolean canBlog;
	private boolean canPrint;
	private boolean canDownload;
	
	public boolean isCanBlog() {
		return canBlog;
	}
	public void setIsCanBlog(boolean canBlog) {
		this.canBlog = canBlog;
	}
	
	public boolean isCanPrint() {
		return canPrint;
	}
	public void setIsCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}
	
	public boolean isCanDownload() {
		return canDownload;
	}
	public void setIsCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}
	
	

}
