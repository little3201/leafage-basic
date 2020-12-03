/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import java.io.Serializable;

/**
 * Enter class for ArticleInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostsVO implements Serializable {

    private static final long serialVersionUID = -2692474466082844624L;
    /**
     * 代码
     */
    private String code;
    /**
     * 点赞
     */
    private int likes;
    /**
     * 评论
     */
    private int comment;
    /**
     * 查看
     */
    private int viewed;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subtitle;
    /**
     * 封面
     */
    private String cover;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
