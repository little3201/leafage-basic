package io.leafage.basic.assets.vo;

/**
 * VO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentVO extends BaseVO {

    private static final long serialVersionUID = -3606281697452944193L;

    /**
     * 帖子
     */
    private String posts;
    /**
     * 昵称
     */
    private String nickname;
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
