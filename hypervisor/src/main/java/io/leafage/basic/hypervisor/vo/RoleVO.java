/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

/**
 * VO class for Role
 *
 * @author liwenqiang
 */
public class RoleVO extends BaseVO {

    private static final long serialVersionUID = 256108084040535709L;
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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
