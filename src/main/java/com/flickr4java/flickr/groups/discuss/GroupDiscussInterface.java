package com.flickr4java.flickr.groups.discuss;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class GroupDiscussInterface {

    /**
     * Group.Discuss Interface.
     * 
     * @author Jonathan Willis
     */
    public static final String METHOD_TOPICS_GET_LIST = "flickr.groups.discuss.topics.getList";

    public static final String METHOD_TOPICS_GET_INFO = "flickr.groups.discuss.topics.getInfo";

    public static final String METHOD_REPLIES_GET_LIST = "flickr.groups.discuss.replies.getList";

    public static final String METHOD_REPLIES_GET_INFO = "flickr.groups.discuss.replies.getInfo";

    private final String apiKey;

    private final String sharedSecret;

    private final Transport transportAPI;

    public GroupDiscussInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get a list of topics from a group.
     * 
     * @param groupId
     *            Unique identifier of a group returns a list of topics for a given group {@link com.flickr4java.flickr.groups.Group}.
     * @param perPage
     *            Number of records per page.
     * @param page
     *            Result-section.
     * @return A group topic list
     * @throws FlickrException
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.discuss.topics.getList.html">API Documentation</a>
     */
    public TopicList<Topic> getTopicsList(String groupId, int perPage, int page) throws FlickrException {
        TopicList<Topic> topicList = new TopicList<Topic>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_TOPICS_GET_LIST);

        parameters.put("group_id", groupId);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element topicElements = response.getPayload();
        topicList.setPage(topicElements.getAttribute("page"));
        topicList.setPages(topicElements.getAttribute("pages"));
        topicList.setPerPage(topicElements.getAttribute("perpage"));
        topicList.setTotal(topicElements.getAttribute("total"));
        topicList.setGroupId(topicElements.getAttribute("group_id"));
        topicList.setIconServer(Integer.parseInt(topicElements.getAttribute("iconserver")));
        topicList.setIconFarm(Integer.parseInt(topicElements.getAttribute("iconfarm")));
        topicList.setName(topicElements.getAttribute("name"));
        topicList.setMembers(Integer.parseInt(topicElements.getAttribute("members")));
        topicList.setPrivacy(Integer.parseInt(topicElements.getAttribute("privacy")));
        topicList.setLanguage(topicElements.getAttribute("lang"));
        topicList.setIsPoolModerated("1".equals(topicElements.getAttribute("ispoolmoderated")));

        NodeList topicNodes = topicElements.getElementsByTagName("topic");
        for (int i = 0; i < topicNodes.getLength(); i++) {
            Element element = (Element) topicNodes.item(i);
            topicList.add(parseTopic(element));
        }
        return topicList;
    }

    /**
     * Get info for a given topic
     * 
     * @param topicId
     *            Unique identifier of a topic for a given group {@link Topic}.
     * @return A group topic
     * @throws FlickrException
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.discuss.topics.getInfo.html">API Documentation</a>
     */
    public Topic getTopicInfo(String topicId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_TOPICS_GET_INFO);
        parameters.put("topic_id", topicId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element topicElement = response.getPayload();

        return parseTopic(topicElement);
    }

    /**
     * Get list of replies
     * 
     * @param topicId
     *            Unique identifier of a topic for a given group {@link Topic}.
     * @return A reply object
     * @throws FlickrException
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.discuss.replies.getList.html">API Documentation</a>
     */
    public ReplyObject getReplyList(String topicId, int perPage, int page) throws FlickrException {
        ReplyList<Reply> reply = new ReplyList<Reply>();
        TopicList<Topic> topic = new TopicList<Topic>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REPLIES_GET_LIST);

        parameters.put("topic_id", topicId);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element replyElements = response.getPayload();
        ReplyObject ro = new ReplyObject();
        NodeList replyNodes = replyElements.getElementsByTagName("reply");
        for (int i = 0; i < replyNodes.getLength(); i++) {
            Element replyNodeElement = (Element) replyNodes.item(i);
            // Element replyElement = XMLUtilities.getChild(replyNodeElement, "reply");
            reply.add(parseReply(replyNodeElement));
            ro.setReplyList(reply);

        }
        NodeList topicNodes = replyElements.getElementsByTagName("topic");
        for (int i = 0; i < topicNodes.getLength(); i++) {
            Element replyNodeElement = (Element) replyNodes.item(i);
            // Element topicElement = XMLUtilities.getChild(replyNodeElement, "topic");
            topic.add(parseTopic(replyNodeElement));
            ro.setTopicList(topic);
        }

        return ro;
    }

    /**
     * Get info for a given topic reply
     * 
     * @param topicId
     *            Unique identifier of a topic for a given group {@link Topic}.
     * @param replyId
     *            Unique identifier of a reply for a given topic {@link Reply}.
     * @return A group topic
     * @throws FlickrException
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.discuss.replies.getInfo.html">API Documentation</a>
     */
    public Reply getReplyInfo(String topicId, String replyId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_REPLIES_GET_INFO);
        parameters.put("topic_id", topicId);
        parameters.put("reply_id", replyId);

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element replyElement = response.getPayload();

        return parseReply(replyElement);
    }

    private Topic parseTopic(Element tElement) {
        Topic topic = new Topic();
        topic.setAuthorId(tElement.getAttribute("author"));
        topic.setAuthorname(tElement.getAttribute("authorname"));
        topic.setIsCanDelete("1".equals(tElement.getAttribute("can_delete")));
        topic.setIsCanEdit("1".equals(tElement.getAttribute("can_edit")));
        topic.setIsCanReply("1".equals(tElement.getAttribute("can_reply")));
        if (!tElement.getAttribute("count_replies").equals("")) {
            topic.setCountReplies(Integer.parseInt(tElement.getAttribute("count_replies")));
        }
        topic.setDatecreate(tElement.getAttribute("datecreate"));
        topic.setDatelastpost(tElement.getAttribute("datelastpost"));
        topic.setIconfarm(Integer.parseInt(tElement.getAttribute("iconfarm")));
        topic.setIconserver(Integer.parseInt(tElement.getAttribute("iconserver")));
        topic.setIsLocked("1".equals(tElement.getAttribute("is_locked")));
        topic.setMessage(XMLUtilities.getChildValue(tElement, "message"));
        topic.setRole(tElement.getAttribute("role"));
        topic.setIsSticky("1".equals(tElement.getAttribute("is_sticky")));
        topic.setSubject(tElement.getAttribute("subject"));
        topic.setTopicId(tElement.getAttribute("id"));
        topic.setIsPro("1".equals(tElement.getAttribute("is_pro")));
        topic.setLastReply(tElement.getAttribute("last_reply"));
        return topic;
    }

    private Reply parseReply(Element rElement) {
        Reply reply = new Reply();
        reply.setAuthorId(rElement.getAttribute("author"));
        reply.setAuthorname(rElement.getAttribute("authorname"));
        reply.setIsCanDelete("1".equals(rElement.getAttribute("can_delete")));
        reply.setIsCanEdit("1".equals(rElement.getAttribute("can_edit")));
        reply.setDatecreate(rElement.getAttribute("datecreate"));
        reply.setLastEdit(rElement.getAttribute("lastedit"));
        reply.setIconfarm(Integer.parseInt(rElement.getAttribute("iconfarm")));
        reply.setIconserver(Integer.parseInt(rElement.getAttribute("iconserver")));
        reply.setMessage(XMLUtilities.getChildValue(rElement, "message"));
        reply.setRole(rElement.getAttribute("role"));
        reply.setReplyId(rElement.getAttribute("id"));
        reply.setIsPro("1".equals(rElement.getAttribute("is_pro")));
        return reply;
    }

}
