package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.BasicBO;

import java.time.LocalDateTime;

/**
 * VO class for Record
 *
 * @author liwenqiang 2022-03-18 21:09
 */
public class AccessLogVO extends BasicBO<String> {

    /**
     * ip
     */
    private String ip;
    /**
     * location
     */
    private String location;
    /**
     * description
     */
    private String description;
    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
