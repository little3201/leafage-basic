/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import io.leafage.basic.assets.bo.CategoryBO;

import java.time.LocalDateTime;

/**
 * VO class for Category
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryVO extends CategoryBO {

    /**
     * 贴子数
     */
    private long count;
    /**
     * 描述
     */
    private String description;
    /**
     * 时间
     */
    private LocalDateTime modifyTime;


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

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
