/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * VO class for Category
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryVO extends BaseVO {

    private static final long serialVersionUID = 6078275280120953852L;
    /**
     * 别名
     */
    private String alias;
    /**
     * 贴子数
     */
    private long count;
    /**
     * 描述
     */
    private String description;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
