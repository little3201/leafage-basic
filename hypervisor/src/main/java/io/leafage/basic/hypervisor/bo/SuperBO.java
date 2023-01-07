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

package io.leafage.basic.hypervisor.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * BO class
 *
 * @author liwenqiang 2022-12-09 22:23
 */
public abstract class SuperBO<T> {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String alias;
    /**
     * 上级
     */
    private SimpleBO<T> superior;
    /**
     * 描述
     */
    @Size(max = 32)
    private String description;

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

    public SimpleBO<T> getSuperior() {
        return superior;
    }

    public void setSuperior(SimpleBO<T> superior) {
        this.superior = superior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
