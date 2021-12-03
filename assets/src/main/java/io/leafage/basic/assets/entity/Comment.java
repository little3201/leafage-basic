package io.leafage.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for comment
 *
 * @author liwenqiang  2021-09-29 10:45
 */
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity {

    /**
     * 帖子ID
     */
    @Column(name = "posts_id")
    private Long postsId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 内容
     */
    private String content;
    /**
     * 回复人
     */
    private String replier;

    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
