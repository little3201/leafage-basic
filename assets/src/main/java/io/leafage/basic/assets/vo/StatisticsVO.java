package io.leafage.basic.assets.vo;

import java.time.LocalDateTime;

/**
 * everyday statistics vo
 *
 * @author liwenqiang 2022-05-25 19:53
 */
public class StatisticsVO {

    /**
     * 帖子
     */
    private String post;
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
     * 时间
     */
    private LocalDateTime modifyTime;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
