/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * VO class for Posts
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class PostsContentVO extends PostsVO {

    /**
     * 帖子ID
     */
    private Long postsId;


    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
    }

}
