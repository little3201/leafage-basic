/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO class for Category
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryVO extends BasicVO<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 8389570117461784047L;

    /**
     * 贴子数
     */
    private long count;
    /**
     * 描述
     */
    private String description;

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
