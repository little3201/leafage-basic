/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * Model class for GroupInfo
 *
 * @author liwenqiang
 */
public class GroupDTO implements Serializable {

    /**
     * 组ID
     */
    private String groupId;
    /**
     * 负责人
     */
    private Long principal;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 中文名
     */
    private String chineseName;
    /**
     * 英文名
     */
    private String englishName;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getPrincipal() {
        return principal;
    }

    public void setPrincipal(Long principal) {
        this.principal = principal;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
}
