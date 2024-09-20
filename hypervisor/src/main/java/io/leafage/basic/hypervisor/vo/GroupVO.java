/*
 *  Copyright 2018-2024 little3201.
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

package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.GroupBO;

import java.time.Instant;

/**
 * vo class for group
 *
 * @author wq li
 */
public class GroupVO extends GroupBO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 上级
     */
    private String superior;

    /**
     * 最后更新时间
     */
    private Instant lastModifiedDate;


    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Long} object
     */
    public Long getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.Long} object
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>superior</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSuperior() {
        return superior;
    }

    /**
     * <p>Setter for the field <code>superior</code>.</p>
     *
     * @param superior a {@link java.lang.String} object
     */
    public void setSuperior(String superior) {
        this.superior = superior;
    }

    /**
     * <p>Getter for the field <code>lastModifiedDate</code>.</p>
     *
     * @return a {@link java.time.Instant} object
     */
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * <p>Setter for the field <code>lastModifiedDate</code>.</p>
     *
     * @param lastModifiedDate a {@link java.time.Instant} object
     */
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
