/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.entity;

import top.abeille.common.basic.BasicInfo;

import javax.persistence.*;

/**
 * Model class for PermInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "perm_info")
public class PermInfo extends BasicInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 权限编号
     */
    @Column(name = "perm_code")
    private String permCode;
    /**
     * 权限父编号
     */
    @Column(name = "perm_parent_code")
    private String permParentCode;
    /**
     * 权限中文名称
     */
    @Column(name = "perm_name_cn")
    private String permNameCn;
    /**
     * 权限英文名称
     */
    @Column(name = "perm_name_en")
    private String permNameEn;
    /**
     * 权限类型
     */
    @Column(name = "perm_type")
    private Integer permType;
    /**
     * 权限路径
     */
    @Column(name = "perm_path")
    private String permPath;
    /**
     * 权限描述
     */
    @Column(name = "perm_desc")
    private String permDesc;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getPermParentCode() {
        return permParentCode;
    }

    public void setPermParentCode(String permParentCode) {
        this.permParentCode = permParentCode;
    }

    public String getPermNameCn() {
        return permNameCn;
    }

    public void setPermNameCn(String permNameCn) {
        this.permNameCn = permNameCn;
    }

    public String getPermNameEn() {
        return permNameEn;
    }

    public void setPermNameEn(String permNameEn) {
        this.permNameEn = permNameEn;
    }

    public Integer getPermType() {
        return permType;
    }

    public void setPermType(Integer permType) {
        this.permType = permType;
    }

    public String getPermPath() {
        return permPath;
    }

    public void setPermPath(String permPath) {
        this.permPath = permPath;
    }

    public String getPermDesc() {
        return permDesc;
    }

    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
    }

}
