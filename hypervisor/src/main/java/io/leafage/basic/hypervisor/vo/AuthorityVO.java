/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;
import top.leafage.common.basic.BasicVO;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO class for Authority
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AuthorityVO extends AbstractVO<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 9207337014543117619L;

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private Character type;
    /**
     * 上级
     */
    private BasicVO<String> superior;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路径
     */
    private String path;
    /**
     * 角色数
     */
    private long count;
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

    public BasicVO<String> getSuperior() {
        return superior;
    }

    public void setSuperior(BasicVO<String> superior) {
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
