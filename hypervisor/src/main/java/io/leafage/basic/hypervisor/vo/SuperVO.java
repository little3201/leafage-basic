/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.SuperBO;

/**
 * VO class
 *
 * @author liwenqiang 2022-12-09 22:23
 */
public abstract class SuperVO<T> extends SuperBO<T> {

    /**
     * 统计数
     */
    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
