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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * BO class for Authority
 *
 * @author liwenqiang 2022-12-10 22:09
 */
public class AuthorityBO {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 类型
     */
    @NotNull
    private Character type;
    /**
     * 上级
     */
    private SimpleBO<String> superior;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路径
     */
    private String path;
    /**
     * 描述
     */
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public SimpleBO<String> getSuperior() {
        return superior;
    }

    public void setSuperior(SimpleBO<String> superior) {
        this.superior = superior;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
