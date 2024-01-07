/*
 *  Copyright 2018-2024 the original author or authors.
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
 * BO class for privilege
 *
 * @author liwenqiang 2022-12-10 22:09
 */
public abstract class ComponentBO {

    /**
     * 名称
     */
    @NotBlank(message = "componentName cannot be blank.")
    @Size(max = 16, message = "componentName max length is 16.")
    private String componentName;

    /**
     * 类型
     */
    @NotNull(message = "type cannot be null.")
    private Character type;

    /**
     * 图标
     */
    @NotBlank(message = "privilege icon is blank.")
    private String icon;

    /**
     * 路径
     */
    private String path;


    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
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
}
