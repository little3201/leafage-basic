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

import io.leafage.basic.hypervisor.bo.UserBO;

import java.time.Instant;

/**
 * vo class for user
 *
 * @author wq li
 */
public class UserVO extends UserBO {

    /**
     * 是否锁定
     */
    private boolean accountNonLocked;

    /**
     * 最后更新时间
     */
    private Instant lastModifiedDate;


    /**
     * <p>isAccountNonLocked.</p>
     *
     * @return a boolean
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * <p>Setter for the field <code>accountNonLocked</code>.</p>
     *
     * @param accountNonLocked a boolean
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
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
