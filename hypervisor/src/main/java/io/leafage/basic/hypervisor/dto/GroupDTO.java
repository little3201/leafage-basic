/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * dto class for group.
 *
 * @author wq li 2019/8/31 15:50
 */
public class GroupDTO implements Serializable {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 上级
     */
    private Long superiorId;

    /**
     * 描述
     */
    @Size(max = 64)
    private String description;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
