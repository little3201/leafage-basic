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

/**
 * Model class
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public abstract class SuperDocument<T> extends AbstractDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private T code;
    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 上级
     */
    @Indexed(sparse = true)
    private ObjectId superior;
    /**
     * 描述
     */
    private String description;


    public T getCode() {
        return code;
    }

    public void setCode(T code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
