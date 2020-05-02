package com.flickr4java.flickr.groups.members;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.util.StringUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Members Interface.
 * 
 * @author mago
 * @version $Id: MembersInterface.java,v 1.1 2009/06/21 19:55:15 x-mago Exp $
 */
public class MembersInterface {
    public static final String METHOD_GET_LIST = "flickr.groups.members.getList";

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

    public MembersInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get a list of the members of a group. The call must be signed on behalf of a Flickr member, and the ability to see the group membership will be
     * determined by the Flickr member's group privileges.
     * 
     * @param groupId
     *            Return a list of members for this group. The group must be viewable by the Flickr member on whose behalf the API call is made.
     * @param memberTypes
     *            A set of Membertypes as available as constants in {@link Member}.
     * @param perPage
     *            Number of records per page.
     * @param page
     *            Result-section.
     * @return A members-list
     * @throws FlickrException if there was a problem connecting to Flickr
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.members.getList.html">API Documentation</a>
     */
    public MembersList<Member> getList(String groupId, Set<String> memberTypes, int perPage, int page) throws FlickrException {
        MembersList<Member> members = new MembersList<Member>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);

        parameters.put("group_id", groupId);

        if (perPage > 0) {
            parameters.put("per_page", "" + perPage);
        }
        if (page > 0) {
            parameters.put("page", "" + page);
        }
        if (memberTypes != null) {
            parameters.put("membertypes", StringUtilities.join(memberTypes, ","));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element mElement = response.getPayload();
        members.setPage(mElement.getAttribute("page"));
        members.setPages(mElement.getAttribute("pages"));
        members.setPerPage(mElement.getAttribute("perpage"));
        members.setTotal(mElement.getAttribute("total"));

        NodeList mNodes = mElement.getElementsByTagName("member");
        for (int i = 0; i < mNodes.getLength(); i++) {
            Element element = (Element) mNodes.item(i);
            members.add(parseMember(element));
        }
        return members;
    }

    private Member parseMember(Element mElement) {
        Member member = new Member();
        member.setId(mElement.getAttribute("nsid"));
        member.setUserName(mElement.getAttribute("username"));
        member.setIconServer(mElement.getAttribute("iconserver"));
        member.setIconFarm(mElement.getAttribute("iconfarm"));
        member.setMemberType(mElement.getAttribute("membertype"));
        return member;
    }
}
