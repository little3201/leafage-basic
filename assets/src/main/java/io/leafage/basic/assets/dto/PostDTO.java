/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * dto class for posts.
 *
 * @author wq li  2019-03-03 22:59
 **/
public class PostDTO {

    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * /**
     * 分类
     */
    @NotNull
    private Long categoryId;
    /**
     * 标签
     */
    @NotEmpty
    private Set<String> tags;
    /**
     * 封面
     */
    @NotBlank
    private String cover;
    /**
     * 内容
     */
    @NotBlank
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
