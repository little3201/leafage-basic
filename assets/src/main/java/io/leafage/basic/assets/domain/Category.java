/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.domain;

import io.leafage.basic.assets.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for category.
 *
 * @author liwenqiang  2020-12-03 22:59
 */
@Entity
@Table(name = "categories", indexes = {@Index(name = "uni_categories_name", columnList = "name")})
public class Category extends AuditMetadata {

    /**
     * 名称
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * 是否可用
     */
    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
