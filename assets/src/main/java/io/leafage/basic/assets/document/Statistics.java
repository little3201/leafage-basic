package io.leafage.basic.assets.document;

public class Statistics extends BaseDocument {

    private long timestamp;

    private long viewed;

    private long likes;

    private long comment;

    public Statistics(long timestamp, long viewed, long likes, long comment) {
        this.timestamp = timestamp;
        this.viewed = viewed;
        this.likes = likes;
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getViewed() {
        return viewed;
    }

    public void setViewed(long viewed) {
        this.viewed = viewed;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }
}
