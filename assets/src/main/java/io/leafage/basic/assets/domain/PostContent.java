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
 * model class for posts content.
 *
 * @author wq li  2020-12-03 22:59
 */
@Entity
@Table(name = "post_content", indexes = {@Index(name = "uni_post_content_post_id", columnList = "post_id")})
public class PostContent extends AuditMetadata {

    /**
     * 帖子ID
     */
    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
