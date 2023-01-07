/*
 *  Copyright 2018-2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Role Authority
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "role_authority")
public class RoleAuthority extends AbstractDocument {

    /**
     * 角色ID
     */
    @Indexed
    @Field(value = "role_id")
    private ObjectId roleId;
    /**
     * 资源ID
     */
    @Indexed
    @Field(value = "authority_id")
    private ObjectId authorityId;


    public ObjectId getRoleId() {
        return roleId;
    }

    public void setRoleId(ObjectId roleId) {
        this.roleId = roleId;
    }

    public ObjectId getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(ObjectId authorityId) {
        this.authorityId = authorityId;
    }
}
