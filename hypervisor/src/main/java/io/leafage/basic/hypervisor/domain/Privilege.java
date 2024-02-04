/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for privilege.
 *
 * @author liwenqiang 2019/8/31 15:50
 */
@Entity
@Table(name = "privileges", indexes = {@Index(name = "uni_privileges_name", columnList = "name")})
public class Privilege extends AuditMetadata {

    /**
     * 名称
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * 上级主键
     */
    private Long superiorId;

    /**
     * 类型
     */
    @Column(name = "type", nullable = false)
    private Character type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路径
     */
    private String path;

    /**
     * 描述
     */
    private String description;


    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
