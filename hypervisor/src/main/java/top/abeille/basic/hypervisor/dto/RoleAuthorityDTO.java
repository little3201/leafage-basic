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
     * 资源code
     */
    private String authorityCode;
    /**
     * 是否可写
     */
    private boolean hasWrite;

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public boolean isHasWrite() {
        return hasWrite;
    }

    public void setHasWrite(boolean hasWrite) {
        this.hasWrite = hasWrite;
    }
}
