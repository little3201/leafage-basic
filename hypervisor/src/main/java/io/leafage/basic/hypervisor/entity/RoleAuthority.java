/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for Role Authority
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "role_authority")
public class RoleAuthority extends AbstractEntity {

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 资源ID
     */
    @Column(name = "authority_id")
    private Long authorityId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }
}
