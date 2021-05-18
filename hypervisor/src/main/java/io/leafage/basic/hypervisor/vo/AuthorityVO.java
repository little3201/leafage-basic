/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

/**
 * VO class for Authority
 *
 * @author liwenqiang
 */
public class AuthorityVO extends BaseVO {

    private static final long serialVersionUID = 7493821282349624572L;
    /**
     * 代码
     */
    private String code;
    /**
     * 上级
     */
    private String superior;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
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
