/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class for GroupInfo
 *
 * @author liwenqiang
 */
public class GroupVO implements Serializable {

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
    /**
     * 修改人
     */
    private Long modifier;
    /**
     * 修改时间
     */
    private LocalDate modifyTime;


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

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDate getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDate modifyTime) {
        this.modifyTime = modifyTime;
    }
}
