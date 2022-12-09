/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for Group
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "group")
public class Group extends SuperDocument<String> {

    /**
     * 负责人
     */
    private ObjectId principal;


    public ObjectId getPrincipal() {
        return principal;
    }

    public void setPrincipal(ObjectId principal) {
        this.principal = principal;
    }

}
