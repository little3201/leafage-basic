/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.document;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * base document
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public abstract class AbstractDocument {

    /**
     * 主键
     */
    @Id
    private ObjectId id;

    /**
     * 是否有效
     */
    @Field(name = "is_enabled")
    private boolean enabled = true;

    /**
     * 修改人(存放 username)
     */
    private String modifier;

    /**
     * 修改时间
     */
    @Field(name = "modify_time")
    @LastModifiedDate
    private LocalDateTime modifyTime;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
