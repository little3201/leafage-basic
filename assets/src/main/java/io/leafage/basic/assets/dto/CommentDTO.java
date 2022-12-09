package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentDTO {

    /**
     * 帖子
     */
    @NotBlank
    private String posts;
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
