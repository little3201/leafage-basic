package top.abeille.basic.hypervisor.dto;

import java.io.Serializable;

public class RoleSourceDTO implements Serializable {

    private static final long serialVersionUID = 1432721617667440537L;

    /**
     * 资源code
     */
    private String sourceCode;
    /**
     * 是否可写
     */
    private Boolean hasWrite;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Boolean getHasWrite() {
        return hasWrite;
    }

    public void setHasWrite(Boolean hasWrite) {
        this.hasWrite = hasWrite;
    }
}
