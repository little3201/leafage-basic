/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import io.leafage.basic.hypervisor.bo.SuperBO;

/**
 * DTO class for Group
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class GroupDTO extends SuperBO<String> {

    /**
     * 负责人
     */
    private String principal;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

}
