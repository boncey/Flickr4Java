package com.flickr4java.flickr.groups.discuss;

import java.util.ArrayList;

public class ReplyObject {

	private ArrayList<Topic> topicList = new ArrayList<Topic>();
	private ArrayList<Reply> replyList = new ArrayList<Reply>();
	
	public ArrayList<Topic> getTopicList() {
		return topicList;
	}
	public void setTopicList(ArrayList<Topic> topicList) {
		this.topicList = topicList;
	}
	public ArrayList<Reply> getReplyList() {
		return replyList;
	}
	public void setReplyList(ArrayList<Reply> replyList) {
		this.replyList = replyList;
	}
	
	
	

}
