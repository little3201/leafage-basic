/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for GroupInfo
 *
 * @author liwenqiang
 */
@Document(collection = "group_info")
public class GroupInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 业务ID
     */
    @Field(value = "business_id")
    @Indexed
    private String businessId;
    /**
     * 负责人
     */
    @Field(value = "principal")
    private String principal;
    /**
     * 上级
     */
    @Field(value = "superior")
    private String superior;
    /**
     * 名称
     */
    @Field(value = "name")
    private String name;
    /**
     * 描述
     */
    @Field(value = "description")
    private String description;
    /**
     * 是否有效
     */
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @Field(value = "modify_time")
    private LocalDateTime modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
