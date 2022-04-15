package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;
import java.io.Serializable;

/**
 * VO class for access log.
 *
 * @author liwenqinag 2022/4/15 13:39
 **/
public class AccessLogVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = -6783030034445738333L;

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
