package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * total statistics vo
 *
 * @author liwenqiang 2022/5/25 19:53
 **/
public class StatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7855604253532295935L;

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
     * 下载量
     */
    private int downloads;

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

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }
}
