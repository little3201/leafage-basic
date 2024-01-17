package io.leafage.basic.hypervisor.vo;

/**
 * vo class for access log.
 *
 * @author liwenqinag 2022/4/15 13:39
 **/
public class AccessLogVO {

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
