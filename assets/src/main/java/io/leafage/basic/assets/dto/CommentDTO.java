package io.leafage.basic.assets.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = -684841439303848020L;

    /**
     * 帖子
     */
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
}
