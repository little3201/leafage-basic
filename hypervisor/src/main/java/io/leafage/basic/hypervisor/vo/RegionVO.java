package io.leafage.basic.hypervisor.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RegionVO implements Serializable {

    private static final long serialVersionUID = -1489536406422335020L;

    /**
     * 代码
     */
    private Long code;
    /**
     * 上级
     */
    private String superior;
    /**
     * 名称
     */
    private String name;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

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

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
