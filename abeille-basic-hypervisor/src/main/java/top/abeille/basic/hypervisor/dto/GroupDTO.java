/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Model class for GroupInfo
 *
 * @author liwenqiang
 */
public class GroupDTO implements Serializable {

    /**
     * 组ID
     */
    @NotNull
    private String groupId;
    /**
     * 负责人
     */
    private Long principal;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 名称
     */
    private String name;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
}
