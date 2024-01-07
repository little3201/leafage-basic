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

package io.leafage.basic.hypervisor.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Model class for region
 *
 * @author liwenqiang 2020-10-06 22:09
 **/
@Table(name = "regions")
public class Region extends AbstractModel {

    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 名称
     */
    @Column(value = "region_name")
    private String regionName;
    /**
     * 上级
     */
    @Column(value = "superior_id")
    private Long superiorId;
    /**
     * 邮编
     */
    @Column(value = "postal_code")
    private Integer postalCode;
    /**
     * 区号
     */
    @Column(value = "area_code")
    private String areaCode;
    /**
     * 是否启用
     */
    @Column(value = "is_enabled")
    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
