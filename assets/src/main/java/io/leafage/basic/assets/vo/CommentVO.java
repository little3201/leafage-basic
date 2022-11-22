package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * VO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3606281697452944193L;

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
    private Instant modifyTime;

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

    public Instant getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Instant modifyTime) {
        this.modifyTime = modifyTime;
    }
}
