/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;
import java.util.Set;

/**
 * VO class for Posts
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class PostsVO extends AbstractVO<String> {

    private static final long serialVersionUID = 5476089760882093211L;

    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 标签
     */
    private Set<String> tags;
    /**
     * 分类
     */
    private String category;
    /**
     * 点赞
     */
    private int likes;
    /**
     * 查看
     */
    private int viewed;
    /**
     * 评论
     */
    private int comments;


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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
