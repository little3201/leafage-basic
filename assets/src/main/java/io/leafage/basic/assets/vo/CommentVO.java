package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;

public class CommentVO extends AbstractVO<String> {

    private static final long serialVersionUID = -1272748016919063381L;

    /**
     * 帖子
     */
    private String posts;
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
