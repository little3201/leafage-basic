/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * VO class for Category
 *
 * @author liwenqiang  2020-12-03 22:59
 */
public class CategoryVO extends BaseVO {

    private static final long serialVersionUID = 6540470230706397453L;

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
