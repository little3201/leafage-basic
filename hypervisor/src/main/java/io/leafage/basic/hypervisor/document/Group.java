/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for Group
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "group")
public class Group extends BaseDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 负责人
     */
    private ObjectId principal;
    /**
     * 上级
     */
    private ObjectId superior;
    /**
     * 名称
     */
    private String name;
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

    public ObjectId getPrincipal() {
        return principal;
    }

    public void setPrincipal(ObjectId principal) {
        this.principal = principal;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
