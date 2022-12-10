package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.AccessLogBO;

import java.time.LocalDateTime;

/**
 * VO class for Record
 *
 * @author liwenqiang 2022-03-18 21:09
 */
public class AccessLogVO extends AccessLogBO {

    private String code;

    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
