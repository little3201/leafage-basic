package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for dictionary.
 *
 * @author liwenqiang 2022-04-06 17:33
 */
@Entity
@Table(name = "dictionaries", indexes = {@Index(name = "uni_dictionaries_name", columnList = "name")})
public class Dictionary extends AuditMetadata {

    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
