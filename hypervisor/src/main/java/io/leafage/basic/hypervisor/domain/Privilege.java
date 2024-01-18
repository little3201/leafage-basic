/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for privilege.
 *
 * @author liwenqiang 2019/8/31 15:50
 */
@Entity
@Table(name = "privileges")
public class Privilege extends AbstractModel {

    /**
     * 上级主键
     */
    private Long superior;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private Character type;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路径
     */
    private String path;
    /**
     * 描述
     */
    private String description;


    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
