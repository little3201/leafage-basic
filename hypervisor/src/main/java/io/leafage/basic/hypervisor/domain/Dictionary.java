package io.leafage.basic.hypervisor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for dictionary.
 *
 * @author liwenqiang 2022-04-06 17:33
 */
@Entity
@Table(name = "dictionary")
public class Dictionary extends AbstractModel {

    /**
     * 主键
     */
    @Column(unique = true)
    private String id;
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
    private String superior;
    /**
     * 描述
     */
    private String description;


    public String getCode() {
        return id;
    }

    public void setCode(Long id) {
        this.id = id;
    }

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

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
