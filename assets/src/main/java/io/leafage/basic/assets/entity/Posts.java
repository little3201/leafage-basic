/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for posts.
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "posts")
public class Posts extends AssetsAbstractEntity {

    /**
     * 标签
     */
    private String tags;
    /**
     * 点赞
     */
    private int likes;
    /**
     * 评论
     */
    private int comments;


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
