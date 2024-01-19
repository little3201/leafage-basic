/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.config.AuditMetadata;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for role.
 *
 * @author liwenqiang 2020-12-20 9:54
 */
@Entity
@Table(name = "roles")
public class Role extends AuditMetadata {

    /**
     * 名称
     */
    private String name;
    /**
     * 上级主键
     */
    private Long superior;
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

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
