package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "statistics")
public class Statistics extends BaseDocument {

    private LocalDate timestamp;

    private int viewed;

    private int likes;

    private int comment;

    public Statistics(LocalDate timestamp, int viewed, int likes, int comment) {
        this.timestamp = timestamp;
        this.viewed = viewed;
        this.likes = likes;
        this.comment = comment;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
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
