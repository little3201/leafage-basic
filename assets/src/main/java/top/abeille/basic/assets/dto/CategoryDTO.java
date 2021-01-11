/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import java.io.Serializable;

/**
 * Model class for AccountInfo
 *
 * @author liwenqiang
 */
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 2516536769852195479L;

    /**
     * 名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
