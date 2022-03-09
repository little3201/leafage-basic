package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;

import java.io.Serializable;

/**
 * VO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentVO extends AbstractVO<String> implements Serializable {

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
     * 回复数
     */
    private long count;

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
