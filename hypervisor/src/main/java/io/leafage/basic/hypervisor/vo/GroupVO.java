/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.time.LocalDateTime;

/**
 * VO class for Group
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class GroupVO extends SuperVO<String> {

    /**
     * 编号
     */
    private String code;
    /**
     * 负责人
     */
    private String principal;
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


    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }


}
