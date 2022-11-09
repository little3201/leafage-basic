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
    private int comments;
    /**
     * 评论量环比
     */
    @Field("over_comments")
    private double overComments;
    /**
     * 下载量
     */
    private int downloads;
    /**
     * 下载量环比
     */
    @Field("over_downloads")
    private double overDownloads;

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

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public double getOverDownloads() {
        return overDownloads;
    }

    public void setOverDownloads(double overDownloads) {
        this.overDownloads = overDownloads;
    }
}
