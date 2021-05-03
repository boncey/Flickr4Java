package com.flickr4java.flickr.tags;

/**
 * 
 * @author mago
 * @version $Id: HotlistTag.java,v 1.2 2009/07/12 22:43:07 x-mago Exp $
 */
public class HotlistTag {

    private String value;

    private int score = 0;

    public HotlistTag() {

    }

    @Deprecated
    // Note that the API no longer returns the score
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setScore(String score) {
        setScore(Integer.parseInt(score));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
