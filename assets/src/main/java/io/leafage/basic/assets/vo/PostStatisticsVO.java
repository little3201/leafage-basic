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

package io.leafage.basic.assets.vo;

/**
 * vo class for statistics.
 *
 * @author wq li  2021-09-03 22:59
 **/
public class PostStatisticsVO {

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 浏览量
     */
    private int viewed;

    /**
     * 浏览量环比
     */
    private double overViewed;

    /**
     * 点赞量
     */
    private int likes;

    /**
     * 浏览量点赞量
     */
    private double overLikes;

    /**
     * 评论量
     */
    private int comments;

    /**
     * 浏览量评论量
     */
    private double overComments;


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

    public double getOverViewed() {
        return overViewed;
    }

    public void setOverViewed(double overViewed) {
        this.overViewed = overViewed;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public double getOverLikes() {
        return overLikes;
    }

    public void setOverLikes(double overLikes) {
        this.overLikes = overLikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public double getOverComments() {
        return overComments;
    }

    public void setOverComments(double overComments) {
        this.overComments = overComments;
    }
}
