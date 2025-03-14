/*
 * Copyright (c) 2024-2025.  little3201.
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

package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.AccessLogBO;
import top.leafage.common.ReadonlyMetadata;

import java.time.Instant;

/**
 * vo class for access log.
 *
 * @author wq li
 */
public class AccessLogVO extends AccessLogBO implements ReadonlyMetadata {

    private final Long id;

    private final boolean enabled;

    private final Instant lastModifiedDate;

    public AccessLogVO(Long id, boolean enabled, Instant lastModifiedDate) {
        this.id = id;
        this.enabled = enabled;
        this.lastModifiedDate = lastModifiedDate;
    }


    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

}
