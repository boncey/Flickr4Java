package com.flickr4java.flickr;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Used to set the credentials for proxy-authentication in {@link REST#setProxy(String, int, String, String)}.
 * 
 * @author mago
 * @version $Id: ProxyAuthenticator.java,v 1.1 2007/11/25 00:26:51 x-mago Exp $
 */
public class ProxyAuthenticator extends Authenticator {
    String userName = "";

    String passWord = "";

    public ProxyAuthenticator(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, passWord.toCharArray());
    }
}
