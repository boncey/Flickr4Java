

package com.flickr4java.flickr.tags;

import java.util.ArrayList;

/**
 * @author Anthony Eden
 */
public class RelatedTagsList extends ArrayList<Tag> {
    private static final long serialVersionUID = 12L;

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
