/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for Group
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "group")
public class Group extends BaseEntity {

    /**
     * 业务ID
     */
    private String code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
