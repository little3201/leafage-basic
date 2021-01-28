package io.leafage.basic.hypervisor.vo;

import java.io.Serializable;

/**
 * 统计对象
 */
public class CountVO implements Serializable {

    private static final long serialVersionUID = -8831838967836102433L;

    /**
     * 代码
     */
    private String code;
    /**
     * 统计数
     */
    private long count;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
