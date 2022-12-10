package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.NotificationBO;

import java.time.LocalDateTime;

/**
 * VO class for Notification
 *
 * @author liwenqiang 2022-02-10 13:53
 */
public class NotificationVO extends NotificationBO {

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
