package com.flickr4java.flickr.groups.members;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Parameter;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.AuthUtilities;
import com.flickr4java.flickr.util.StringUtilities;

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

    public MembersInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get a list of the members of a group.
     * The call must be signed on behalf of a Flickr member,
     * and the ability to see the group membership will be
     * determined by the Flickr member's group privileges.
     *
     * @param groupId Return a list of members for this group. The group must be viewable by the Flickr member on whose behalf the API call is made.
     * @param memberTypes A set of Membertypes as available as constants in {@link Member}.
     * @param perPage Number of records per page.
     * @param page Result-section.
     * @return A members-list
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.members.getList.html">API Documentation</a>
     */
    public MembersList getList(String groupId, Set memberTypes, int perPage, int page)
      throws FlickrException, IOException, SAXException {
        MembersList members = new MembersList();
        List parameters = new ArrayList();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("group_id", groupId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }
        if (memberTypes != null) {
            parameters.add(
                new Parameter(
                    "membertypes",
                    StringUtilities.join(memberTypes, ",")
                )
            );
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );
        Response response = transportAPI.get(transportAPI.getPath(), parameters);
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
