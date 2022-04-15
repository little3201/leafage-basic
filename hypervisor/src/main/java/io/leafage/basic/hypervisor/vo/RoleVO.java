/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;
import java.io.Serializable;

/**
 * VO class for role.
 *
 * @author liwenqiang 2019/8/31 15:50
 */
public class RoleVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = 256108084040535709L;

    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private String superior;
    /**
     * 描述
     */
    private String description;
    /**
     * 账号数
     */
    private long count;
    /**
     * 是否可用
     */
    private boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
