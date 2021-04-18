/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for ResourceInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "authority")
public class Authority extends BaseDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 上级
     */
    private ObjectId superior;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 路径
     */
    private String path;
    /**
     * 请求方式, 如：GET、POST、PUT、DELETE等
     */
    private String mode;
    /**
     * 描述
     */
    private String description;

    enum Type {
        /**
         * 菜单
         */
        MENU,
        /**
         * 按钮
         */
        BTN,
        /**
         * 接口
         */
        @JsonEnumDefaultValue
        ROUTER
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ObjectId getSuperior() {
        return superior;
    }

    public void setSuperior(ObjectId superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type.toUpperCase()).name();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
