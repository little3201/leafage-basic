/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * VO for Resource
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class ResourceVO extends PostVO {

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
