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
 * Model class for Account Group
 *
 * @author liwenqiang 2019/9/16 10:09
 **/
@Document(collection = "account_group")
public class AccountGroup extends AbstractDocument {

    /**
     * 账号主键
     */
    @Indexed
    @Field(value = "account_id")
    private ObjectId accountId;
    /**
     * 分组主键
     */
    @Indexed
    @Field(value = "group_id")
    private ObjectId groupId;


    public ObjectId getAccountId() {
        return accountId;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public ObjectId getGroupId() {
        return groupId;
    }

    public void setGroupId(ObjectId groupId) {
        this.groupId = groupId;
    }

}
