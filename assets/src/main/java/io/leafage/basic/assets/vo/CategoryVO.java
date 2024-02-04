/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * vo class for category.
 *
 * @author wq li  2020-12-03 22:59
 */
public class CategoryVO {

    /**
     * 名称
     */
    private String name;
    /**
     * 统计数
     */
    private long count;
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
