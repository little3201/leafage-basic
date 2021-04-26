/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Model class for Posts
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "posts")
public class Posts extends BaseEntity {

    /**
     * 唯一标识
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * 概览
     */
    private String subtitle;
    /**
     * 分类
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    /**
     * 封面
     */
    private String cover;
    /**
     * 标签
     */
    private Set<String> tags;
    /**
     * 点赞
     */
    private int likes;
    /**
     * 查看
     */
    private int viewed;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
}
