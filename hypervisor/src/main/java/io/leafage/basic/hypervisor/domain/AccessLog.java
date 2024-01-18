package io.leafage.basic.hypervisor.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for access log.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
@Entity
@Table(name = "access_logs")
public class AccessLog extends AbstractModel {

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
