/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.groups.members.Member;
import com.flickr4java.flickr.groups.members.MembersInterface;
import com.flickr4java.flickr.groups.members.MembersList;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mago
 * @version $Id: MembersInterfaceTest.java,v 1.3 2009/07/11 20:30:27 x-mago Exp $
 */
public class MembersInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetList() throws FlickrException {
        MembersInterface iface = flickr.getMembersInterface();
        // Group: Urban fragments
        String id = testProperties.getGroupId();
        Set<String> memberTypes = new HashSet<String>();
        memberTypes.add(Member.TYPE_MEMBER);
        memberTypes.add(Member.TYPE_ADMIN);
        memberTypes.add(Member.TYPE_MODERATOR);
        MembersList<Member> list = iface.getList(id, memberTypes, 50, 1);
        assertNotNull(list);
        assertEquals(50, list.size());
        Member m = list.get(10);
        assertTrue(m.getId().indexOf("@") > 0);
        assertTrue(m.getUserName().length() > 0);
        assertTrue(m.getIconFarm() > -1);
        assertTrue(m.getIconServer() > -1);
    }
}
