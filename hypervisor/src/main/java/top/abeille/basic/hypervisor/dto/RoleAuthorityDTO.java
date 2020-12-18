package top.abeille.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * Model class for RoleResource
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class RoleAuthorityDTO implements Serializable {

    private static final long serialVersionUID = 1432721617667440537L;

    /**
     * 权限 code
     */
    private String code;
    /**
     * 是否可写
     */
    private String mode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
