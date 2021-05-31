/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * DTO class for User Group
 *
 * @author liwenqiang 2021-06-01 07:27
 */
public class UserGroupDTO implements Serializable {

    private static final long serialVersionUID = -7052161926646480251L;

    /**
     * 账号
     */
    @NotBlank
    private String username;
    /**
     * 分组
     */
    @NotEmpty
    private Set<String> groups;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }
}
