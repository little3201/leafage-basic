package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;

@Document(collection = "statistics")
public class Statistics extends AbstractDocument {

    /**
     * 记录日期
     */
    private LocalDate date;
    /**
     * 浏览量
     */
    private int viewed;
    /**
     * 浏览量环比
     */
    @Field("over_viewed")
    private double overViewed;
    /**
     * 点赞量
     */
    private int likes;
    /**
     * 点赞量环比
     */
    @Field("over_likes")
    private double overLikes;
    /**
     * 评论量
     */
    private int comment;
    /**
     * 评论量环比
     */
    @Field("over_comment")
    private double overComment;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public double getOverViewed() {
        return overViewed;
    }

    public void setOverViewed(double overViewed) {
        this.overViewed = overViewed;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public double getOverLikes() {
        return overLikes;
    }

    public void setOverLikes(double overLikes) {
        this.overLikes = overLikes;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public double getOverComment() {
        return overComment;
    }

    public void setOverComment(double overComment) {
        this.overComment = overComment;
    }
}
