package io.leafage.basic.hypervisor.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO class for access log
 *
 * @author liwenqiang 2022-03-18 21:09
 */
public class AccessLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5527869037538528806L;

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
