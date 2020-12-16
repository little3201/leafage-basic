/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * document for ArticleInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "posts_content")
public class PostsContent {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 文章ID
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
    /**
     * 源文本——记录富文本或markdown原文
     */
    private String original;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
