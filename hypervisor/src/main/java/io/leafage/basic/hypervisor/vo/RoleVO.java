/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package io.leafage.basic.hypervisor.vo;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class RoleVO extends BaseVO {

    private static final long serialVersionUID = 1564574233487923178L;

    /**
     * 名称
     */
    private String name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
