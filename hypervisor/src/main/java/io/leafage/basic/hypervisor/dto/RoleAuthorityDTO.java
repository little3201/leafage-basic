/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO class for Role Authority
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class RoleAuthorityDTO implements Serializable {

    private static final long serialVersionUID = 1432721617667440537L;

    /**
     * 权限 code
     */
    @NotBlank
    private String code;
    /**
     * 是否可写
     */
    private String mode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
