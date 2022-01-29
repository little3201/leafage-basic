/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * VO class for posts content.
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class PostsContentVO extends PostsVO {

    /**
     * 帖子ID
     */
    private Long postsId;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 内容
     */
    private String content;


    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
