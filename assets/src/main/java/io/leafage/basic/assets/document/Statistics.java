package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "statistics")
public class Statistics extends BaseDocument {

    private LocalDate date;

    private int viewed;

    private int likes;

    private int comment;

    public Statistics(LocalDate date, int viewed, int likes, int comment) {
        this.date = date;
        this.viewed = viewed;
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
