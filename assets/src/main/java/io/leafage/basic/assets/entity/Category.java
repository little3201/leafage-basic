/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for category
 *
 * @author liwenqiang  2020-12-03 22:59
 */
@Entity
@Table(name = "category")
public class Category extends BaseEntity {


    /**
     * 唯一标识
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
