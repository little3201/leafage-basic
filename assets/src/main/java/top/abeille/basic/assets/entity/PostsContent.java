/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * document for posts
 *
 * @author liwenqiang  2020-12-03 22:59
 */
@Entity
@Table(name = "posts_content")
public class PostsContent extends BaseEntity {

    /**
     * 帖子ID
     */
    @Column(name = "posts_id")
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
