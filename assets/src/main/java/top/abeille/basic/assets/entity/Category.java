/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for category
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "category")
public class Category extends BaseEntity {


    /**
     * 唯一标识
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    /**
     * 名称
     */
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
