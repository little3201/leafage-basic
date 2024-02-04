package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for region.
 *
 * @author wq li 2021-10-12 10:06
 */
@Entity
@Table(name = "regions")
public class Region extends AuditMetadata {

    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 上级
     */
    @Column(name = "superior_id")
    private Long superiorId;

    /**
     * 区号
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 邮编
     */
    @Column(name = "postal_code")
    private String postalCode;

    /**
     * 描述
     */
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
