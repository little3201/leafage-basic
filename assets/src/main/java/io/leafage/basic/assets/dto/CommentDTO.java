package io.leafage.basic.assets.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * dto class for comment.
 *
 * @author wq li 2021-09-29 13:51
 */
public class CommentDTO {

    /**
     * 帖子
     */
    @NotBlank
    private String post;
    /**
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 回复人
     */
    private String replier;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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
