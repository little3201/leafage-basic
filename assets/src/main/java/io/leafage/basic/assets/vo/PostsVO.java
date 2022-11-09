/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * VO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostsVO extends AbstractVO<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2692474466082844624L;

    /**
     * 标题
     */
    private String title;
    /**
     * 封面¬
     */
    private String cover;
    /**
     * 标签
     */
    private Set<String> tags;
    /**
     * 分类
     */
    private BasicVO<String> category;
    /**
     * 点赞
     */
    private int likes;
    /**
     * 评论
     */
    private int comments;
    /**
     * 查看
     */
    private int viewed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public BasicVO<String> getCategory() {
        return category;
    }

    public void setCategory(BasicVO<String> category) {
        this.category = category;
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

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
