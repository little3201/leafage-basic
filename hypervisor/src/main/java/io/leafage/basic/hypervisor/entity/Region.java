package io.leafage.basic.hypervisor.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for Region
 *
 * @author liwenqiang  2021-10-12 10:06
 */
@Entity
@Table(name = "region")
public class Region extends BaseEntity {

    /**
     * 代码
     */
    private Long code;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 名称
     */
    private String name;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
