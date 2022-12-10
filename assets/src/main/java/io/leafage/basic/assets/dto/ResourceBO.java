/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import io.leafage.basic.assets.bo.SuperBO;

/**
 * DTO class for Resource
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class ResourceBO extends SuperBO {

    /**
     * 类型
     */
    private Character type;
    /**
     * 描述
     */
    private String description;

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