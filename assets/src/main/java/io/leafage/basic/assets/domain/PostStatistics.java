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
 * model class for post statistics.
 *
 * @author wq li  2021-09-29 10:45
 */
@Entity
@Table(name = "post_statistics", indexes = {@Index(name = "idx_post_statistics_post_id", columnList = "post_id")})
public class PostStatistics extends AuditMetadata {

    /**
     * 帖子ID
     */
    @Column(name = "post_id", nullable = false)
    private Long postId;

    /**
     * 浏览量
     */
    private int viewed;

    /**
     * 点赞量
     */
    private int likes;

    /**
     * 评论量
     */
    private int comments;


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

}
