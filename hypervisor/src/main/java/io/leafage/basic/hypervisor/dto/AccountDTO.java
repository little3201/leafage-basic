/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * DTO class for Account
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 5424195124842285237L;

    /**
     * 类型
     */
    private char type;
    /**
     * 修改人
     */
    private String modifier;

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
