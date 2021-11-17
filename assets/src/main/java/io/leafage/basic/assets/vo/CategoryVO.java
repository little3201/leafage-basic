/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;

/**
 * VO class for Category
 *
 * @author liwenqiang  2020-12-03 22:59
 */
public class CategoryVO extends AbstractVO<String> {

    private static final long serialVersionUID = 6540470230706397453L;

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
