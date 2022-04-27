/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * DTO class for Resource
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class ResourceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6514393945624239153L;
    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * cover
     */
    @NotBlank
    private String cover;
    /**
     * 分类
     */
    @NotBlank
    private String category;
    /**
     * 类型
     */
    private Character type;
    /**
     * 描述
     */
    private String description;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
