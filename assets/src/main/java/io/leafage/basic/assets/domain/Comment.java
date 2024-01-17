package io.leafage.basic.assets.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for comment.
 *
 * @author liwenqiang  2021-09-29 10:45
 */
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity {

    /**
     * id
     */
    @Column(unique = true)
    private String id;
    /**
     * 帖子ID
     */
    @Column(name = "posts_id")
    private Long postsId;
    /**
     * 国家
     */
    private String country;
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
    private String replier;


    public String getCode() {
        return id;
    }

    public void setCode(Long id) {
        this.id = id;
    }

    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }
}
