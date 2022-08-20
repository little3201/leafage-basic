/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO class for Group
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class GroupVO extends AbstractVO<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5740100575689452491L;

    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 上级
     */
    private BasicVO<String> superior;
    /**
     * 描述
     */
    private String description;
    /**
     * 用户数
     */
    private long count;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public BasicVO<String> getSuperior() {
        return superior;
    }

    public void setSuperior(BasicVO<String> superior) {
        this.superior = superior;
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
