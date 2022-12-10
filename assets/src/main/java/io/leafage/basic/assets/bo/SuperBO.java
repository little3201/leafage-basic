/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * DTO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public abstract class SuperBO {

    /**
     * 标题
     */
    @NotBlank
    private String title;
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
    private CategoryBO category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public CategoryBO getCategory() {
        return category;
    }

    public void setCategory(CategoryBO category) {
        this.category = category;
    }
}
