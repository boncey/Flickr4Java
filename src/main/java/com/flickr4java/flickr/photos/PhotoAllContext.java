package com.flickr4java.flickr.photos;

public class PhotoAllContext {
	
	private PhotoSetList<PhotoSet> photoSetList = new PhotoSetList<PhotoSet>();
	private PoolList<Pool> poolList = new PoolList<Pool>();
	
	public PhotoSetList<PhotoSet> getPhotoSetList() {
		return photoSetList;
	}
	public void setPhotoSetList(PhotoSetList<PhotoSet> photoSetList) {
		this.photoSetList = photoSetList;
	}
	public PoolList<Pool> getPoolList() {
		return poolList;
	}
	public void setPoolList(PoolList<Pool> poolList) {
		this.poolList = poolList;
	}

}
