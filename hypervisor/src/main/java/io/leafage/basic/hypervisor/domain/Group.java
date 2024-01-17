/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for group.
 *
 * @author liwenqiang 2020-12-20 9:54
 */
@Entity
@Table(name = "group_")
public class Group extends AbstractModel {

    /**
     * 名称
     */
    private String name;
    /**
     * 负责人主键
     */
    private Long principal;
    /**
     * 上级主键
     */
    private Long superior;
    /**
     * 描述
     */
    private String description;


    public Long getPrincipal() {
        return principal;
    }

    public void setPrincipal(Long principal) {
        this.principal = principal;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
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
