/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.domain;

import io.leafage.basic.assets.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for posts.
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "posts", indexes = {@Index(name = "idx_posts_category_id", columnList = "category_id"),
        @Index(name = "idx_posts_created_by", columnList = "created_by")})
public class Post extends AuditMetadata {

    /**
     * 标题
     */
    @Column(name = "title", nullable = false, unique = true)
    private String title;

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
    private String tags;

    /**
     * 是否可用
     */
    @Column(name = "is_enabled")
    private boolean enabled = true;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
