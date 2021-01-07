/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for AccountInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    /**
     * 业务ID
     */
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
