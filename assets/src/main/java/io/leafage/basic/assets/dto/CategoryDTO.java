/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Model class for Category
 *
 * @author liwenqiang  2020-12-03 22:59
 */
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 2516536769852195479L;

    /**
     * 名称
     */
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
