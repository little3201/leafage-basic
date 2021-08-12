/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO class for Account
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 5424195124842285237L;

    /**
     * 代码（卡号）
     */
    @NotBlank
    private String code;
    /**
     * 类型
     */
    private int type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
