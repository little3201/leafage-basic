package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;

/**
 * VO class for comment.
 *
 * @author liwenqiang  2021-08-03 22:59
 **/
public class CommentVO extends AbstractVO<String> {

    private static final long serialVersionUID = -1272748016919063381L;

    /**
     * 帖子
     */
    private String posts;
    /**
     * 内容
     */
    private String content;
    /**
     * 国家
     */
    private String country;
    /**
     * 位置
     */
    private String location;
    /**
     * 回复数
     */
    private Long count;

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
