package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.config.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for region.
 *
 * @author liwenqiang 2021-10-12 10:06
 */
@Entity
@Table(name = "regions")
public class Region extends AuditMetadata {

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String alias;

    /**
     * 上级
     */
    @Column(name = "superior_id")
    private Long superiorId;

    /**
     * 邮编
     */
    @Column(name = "postal_id")
    private String postalCode;

    /**
     * 区号
     */
    @Column(name = "area_id")
    private String areaCode;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
