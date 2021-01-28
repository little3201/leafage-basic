/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 2513250238715183575L;
    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 描述
     */
    @Size(max = 32)
    private String description;
    /**
     * 资源列表
     */
    private Set<String> authorities = Collections.emptySet();
    /**
     * 修改人
     */
    private String modifier;

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

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
