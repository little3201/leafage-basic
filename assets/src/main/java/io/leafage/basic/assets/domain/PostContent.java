/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.domain;

import io.leafage.basic.assets.config.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for posts content.
 *
 * @author liwenqiang  2020-12-03 22:59
 */
@Entity
@Table(name = "post_content")
public class PostContent extends AuditMetadata {

    /**
     * 帖子ID
     */
    @Column(name = "post_id", nullable = false)
    private Long postId;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 内容
     */
    private String content;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
