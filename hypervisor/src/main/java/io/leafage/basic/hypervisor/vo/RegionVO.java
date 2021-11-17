package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;

public class RegionVO extends AbstractVO<Long> {

    private static final long serialVersionUID = -1489536406422335020L;

    /**
     * 上级
     */
    private String superior;
    /**
     * 名称
     */
    private String name;

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
