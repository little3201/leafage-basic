package io.leafage.basic.hypervisor.vo;

import java.io.Serializable;

/**
 * 统计对象
 */
public class CountVO implements Serializable {

    private static final long serialVersionUID = -8831838967836102433L;

    /**
     * 主键ID
     */
    private String id;
    /**
     * 统计数
     */
    private long count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
