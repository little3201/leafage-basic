/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VO class for Category
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoriesVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8389570117461784047L;

    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
