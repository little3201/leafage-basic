/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for SourceInfo
 *
 * @author liwenqiang
 */
@Document(collection = "source_info")
public class SourceInfo {

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
     * 上级
     */
    @Field(value = "superior")
    private String superior;
    /**
     * 名称
     */
    @Indexed
    @Field(value = "name")
    private String name;
    /**
     * 类型
     */
    @Field(value = "type")
    private Integer type;
    /**
     * 路径
     */
    @Field(value = "path")
    private String path;
    /**
     * 描述
     */
    @Field(value = "description")
    private String description;

    /**
     * 是否有效
     */
    @JsonIgnore
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @JsonIgnore
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @JsonIgnore
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
