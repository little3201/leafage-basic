package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;
import java.io.Serializable;

/**
 * VO class for region.
 *
 * @author liwenqiang 2021/9/19 17:20
 **/
public class RegionVO extends AbstractVO<Long> implements Serializable {

    private static final long serialVersionUID = -1489536406422335020L;

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
     * 邮编
     */
    private String zip;
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

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
