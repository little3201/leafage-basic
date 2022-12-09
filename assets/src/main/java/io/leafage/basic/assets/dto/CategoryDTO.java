/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for Category
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryDTO {

    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 描述
     */
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
