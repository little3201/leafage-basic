package io.leafage.basic.assets.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO class for Comment
 *
 * @author liwenqiang 2021-09-29 13:51
 */
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = -9074666319515837124L;

    /**
     * 帖子
     */
    @NotBlank
    private String posts;
    /**
     * 昵称
     */
    @NotBlank
    private String nickname;
    /**
     * 邮箱
     */
    @NotBlank
    @Email
    private String email;
    /**
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 回复人
     */
    private String replier;

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
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
