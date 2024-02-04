/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * dto class for role.
 *
 * @author wq li 2019/8/31 15:50
 */
public class RoleDTO implements Serializable {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 上级
     */
    private Long superiorId;
    /**
     * 描述
     */
    @Size(max = 32)
    private String description;
    /**
     * 资源列表
     */
    private Set<String> privileges;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getAuthorities() {
        return privileges;
    }

    public void setAuthorities(Set<String> privileges) {
        this.privileges = privileges;
    }

}
