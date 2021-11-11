/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for Role
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "role")
public class Role extends BaseDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    @Indexed(sparse = true)
    private ObjectId superior;
    /**
     * 描述
     */
    private String description;


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

    public ObjectId getSuperior() {
        return superior;
    }

    public void setSuperior(ObjectId superior) {
        this.superior = superior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
