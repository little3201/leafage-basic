/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseVO implements Serializable {

    private static final long serialVersionUID = -5454263056607645027L;

    /**
     *
     * 代码
     */
    private String code;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
