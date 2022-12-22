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
