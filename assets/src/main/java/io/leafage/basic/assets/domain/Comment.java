/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.assets.domain;

import io.leafage.basic.assets.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for comment.
 *
 * @author wq li
 */
@Entity
@Table(name = "comments", indexes = {@Index(name = "idx_comments_post_id", columnList = "post_id")})
public class Comment extends AuditMetadata {

    /**
     * 帖子ID
     */
    @Column(name = "post_id", nullable = false)
    private Long postId;

    /**
     * 位置
     */
    private String location;

    /**
     * 内容
     */
    private String content;

    /**
     * 回复人
     */
    private Long replier;


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReplier() {
        return replier;
    }

    public void setReplier(Long replier) {
        this.replier = replier;
    }
}
