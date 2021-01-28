package io.leafage.basic.assets.vo;

import java.io.Serializable;

/**
 * 统计对象
 */
public class CountVO implements Serializable {

    private static final long serialVersionUID = -4797875455929965026L;

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
