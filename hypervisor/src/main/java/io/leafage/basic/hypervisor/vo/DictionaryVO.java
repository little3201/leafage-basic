/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.time.LocalDateTime;

/**
 * VO class for Dictionary
 *
 * @author liwenqiang 2022-03-30 07:27
 **/
public class DictionaryVO extends SuperVO<String> {


    /**
     * 编号
     */
    private String code;
    /**
     * 更新时间
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
