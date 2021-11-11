/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * DTO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostsDTO implements Serializable {

    private static final long serialVersionUID = 248576207213923230L;
    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 封面
     */
    @NotBlank
    private String cover;
    /**
     * 标签
     */
    @NotEmpty
    private Set<String> tags;
    /**
     * 分类
     */
    @NotBlank
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
