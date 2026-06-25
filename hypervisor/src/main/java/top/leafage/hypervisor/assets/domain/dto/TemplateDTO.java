/*
 * Copyright (c) 2024-2026.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.assets.domain.dto;

import jakarta.validation.constraints.NotBlank;
import top.leafage.hypervisor.assets.domain.Template;

/**
 * dto class for template.
 *
 * @author wq li
 */
public class TemplateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String type;


    public static Template toEntity(TemplateDTO dto) {
        return new Template(
                dto.getName(),
                dto.getType()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
