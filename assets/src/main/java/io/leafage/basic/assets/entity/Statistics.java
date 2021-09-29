package io.leafage.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Model class for statistics
 *
 * @author liwenqiang  2021-09-29 10:45
 */
@Entity
@Table(name = "statistics")
public class Statistics extends BaseEntity {

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
    @Column(name = "over_viewed")
    private double overViewed;
    /**
     * 点赞量
     */
    private int likes;
    /**
     * 浏览量点赞量
     */
    @Column(name = "over_likes")
    private double overLikes;
    /**
     * 评论量
     */
    private int comment;
    /**
     * 浏览量评论量
     */
    @Column(name = "over_comment")
    private double overComment;

    public Statistics(LocalDate date, int viewed, double overViewed, int likes, double overLikes, int comment, double overComment) {
        this.date = date;
        this.viewed = viewed;
        this.overViewed = overViewed;
        this.likes = likes;
        this.overLikes = overLikes;
        this.comment = comment;
        this.overComment = overComment;
    }

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
