package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;

@Document(collection = "statistics")
public class Statistics extends BaseDocument {

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
    private int overViewed;
    /**
     * 点赞量
     */
    private int likes;
    /**
     * 评论量
     */
    private int comment;

    public Statistics(LocalDate date, int viewed, int overViewed, int likes, int comment) {
        this.date = date;
        this.viewed = viewed;
        this.overViewed = overViewed;
        this.likes = likes;
        this.comment = comment;
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
