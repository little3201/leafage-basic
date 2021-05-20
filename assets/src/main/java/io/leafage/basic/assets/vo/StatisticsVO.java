package io.leafage.basic.assets.vo;

public class StatisticsVO extends BaseVO {

    private static final long serialVersionUID = 4288475041155960116L;

    /**
     * 浏览量
     */
    private int viewed;
    /**
     * 浏览量环比
     */
    private int overViewed;
    /**
     * 点赞量
     */
    private int likes;
    /**
     * 评论量
     */
    private int comment;

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getOverViewed() {
        return overViewed;
    }

    public void setOverViewed(int overViewed) {
        this.overViewed = overViewed;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
