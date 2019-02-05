
package com.flickr4java.flickr.blogs;

import java.math.BigDecimal;

/**
 * Class representing a Flickr blog configuration.
 * 
 * @author Anthony Eden
 */
public class Blog {

    private BigDecimal id;

    private String name;

    private boolean needPassword;

    private String url;

    public Blog() {

    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public void setId(String id) {
        if (id != null) {
            setId(new BigDecimal(id));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(boolean needPassword) {
        this.needPassword = needPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
