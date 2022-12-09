package io.leafage.basic.hypervisor.vo;

/**
 * VO class for Record
 *
 * @author liwenqiang 2022-03-18 21:09
 */
public class AccessLogVO extends AbstractVO<String> {

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
