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

import top.leafage.common.servlet.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for post statistics.
 *
 * @author wq li
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


    /**
     * <p>Getter for the field <code>postId</code>.</p>
     *
     * @return a {@link java.lang.Long} object
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * <p>Setter for the field <code>postId</code>.</p>
     *
     * @param postId a {@link java.lang.Long} object
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    /**
     * <p>Getter for the field <code>viewed</code>.</p>
     *
     * @return a int
     */
    public int getViewed() {
        return viewed;
    }

    /**
     * <p>Setter for the field <code>viewed</code>.</p>
     *
     * @param viewed a int
     */
    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    /**
     * <p>Getter for the field <code>likes</code>.</p>
     *
     * @return a int
     */
    public int getLikes() {
        return likes;
    }

    /**
     * <p>Setter for the field <code>likes</code>.</p>
     *
     * @param likes a int
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * <p>Getter for the field <code>comments</code>.</p>
     *
     * @return a int
     */
    public int getComments() {
        return comments;
    }

    /**
     * <p>Setter for the field <code>comments</code>.</p>
     *
     * @param comments a int
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

}
