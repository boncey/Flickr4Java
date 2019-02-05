
package com.flickr4java.flickr.tags;

/**
 * @author Anthony Eden
 */
public class Tag {

    private String id;

    private String author;

    private String authorName;

    private String raw;

    private String value;

    private int count;

    public Tag() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCount(String count) {
        setCount(Integer.parseInt(count));
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Tag test = (Tag) obj;
        return count == test.count && areEqual(value, test.value) && areEqual(raw, test.raw) && areEqual(author, test.author)
                && areEqual(authorName, test.authorName) && areEqual(id, test.id);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash += new Integer(count).hashCode();
        if (value != null) {
            hash += value.hashCode();
        }
        if (raw != null) {
            hash += raw.hashCode();
        }
        if (author != null) {
            hash += author.hashCode();
        }
        if (authorName != null) {
            hash += authorName.hashCode();
        }
        if (id != null) {
            hash += id.hashCode();
        }
        return hash;
    }

    private boolean areEqual(Object x, Object y) {
        return x == null ? y == null : x.equals(y);
    }

    @Override
    public String toString() {
        return String.format("Tag [value=%s, count=%s]", value, count);
    }
}
