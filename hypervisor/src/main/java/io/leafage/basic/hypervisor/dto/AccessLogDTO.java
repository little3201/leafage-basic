package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * DTO class for access log.
 *
 * @author liwenqinag 2022/4/15 13:39
 **/
public class AccessLogDTO implements Serializable {

    private static final long serialVersionUID = 451126769823410852L;

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
}
