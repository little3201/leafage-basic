/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for category.
 *
 * @author liwenqiang  2020-12-03 22:59
 */
@Entity
@Table(name = "category")
public class Category extends AbstractEntity {

    /**
     * 名称
     */
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
