package io.leafage.basic.assets.vo;

public class StatisticsVO extends BaseVO {

    private static final long serialVersionUID = 4288475041155960116L;

    private long viewed;

    private long likes;

    private long comment;

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
