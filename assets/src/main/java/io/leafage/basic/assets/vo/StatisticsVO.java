package io.leafage.basic.assets.vo;

import java.io.Serializable;
import java.time.LocalDate;

public class StatisticsVO implements Serializable {

    private static final long serialVersionUID = 4288475041155960116L;

    /**
     * 统计日期
     */
    private LocalDate date;
    /**
     * 浏览量
     */
    private int viewed;
    /**
     * 浏览量环比
     */
    private double overViewed;
    /**
     * 点赞量
     */
    private int likes;
    /**
     * 点赞量环比
     */
    private double overLikes;
    /**
     * 评论量
     */
    private int comments;
    /**
     * 评论量环比
     */
    private double overComments;

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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public double getOverComments() {
        return overComments;
    }

    public void setOverComments(double overComments) {
        this.overComments = overComments;
    }
}
