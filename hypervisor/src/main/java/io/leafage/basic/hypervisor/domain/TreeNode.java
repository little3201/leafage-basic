package io.leafage.basic.hypervisor.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TreeNode implements Serializable {

    private static final long serialVersionUID = -1764596537713443023L;

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private String superior;
    /**
     * 扩展属性
     */
    private Map<String, String> expand;
    /**
     * 子节点
     */
    private List<TreeNode> children;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public Map<String, String> getExpand() {
        return expand;
    }

    public void setExpand(Map<String, String> expand) {
        this.expand = expand;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
