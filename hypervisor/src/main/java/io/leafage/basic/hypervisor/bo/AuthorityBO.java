/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * BO class for Authority
 *
 * @author liwenqiang 2022-12-10 22:09
 */
public class AuthorityBO {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 类型
     */
    @NotNull
    private Character type;
    /**
     * 上级
     */
    private BasicBO<String> superior;
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

    public BasicBO<String> getSuperior() {
        return superior;
    }

    public void setSuperior(BasicBO<String> superior) {
        this.superior = superior;
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
