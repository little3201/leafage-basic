/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

/**
 * Model class for GroupInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class GroupVO extends BaseVO {

    private static final long serialVersionUID = 5740100575689452491L;

    /**
     * 负责人
     */
    private String principal;
    /**
     * 上级
     */
    private String superior;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 用户数
     */
    private long count;


    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
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
