/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * DTO class for Category
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4716083233926076297L;

    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    @NotBlank
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
