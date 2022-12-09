package io.leafage.basic.assets.vo;

import java.time.LocalDateTime;

/**
 * VO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentVO {

    /**
     * 编号
     */
    private String code;
    /**
     * 帖子code
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
    private long count;
    /**
     * 时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
