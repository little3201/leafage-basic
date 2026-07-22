/*
 * Copyright(c) 2019-present the original author or authors.
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

package top.leafage.hypervisor.audits.domain.vo;

import top.leafage.hypervisor.audits.domain.AuditLog;

import java.util.Map;

/**
 * vo class for audit log.
 *
 * @author wq li
 */
public record AuditLogVO(
        Long id,
        String module,
        String action,
        Long targetId,
        Map<String, Object> oldValue,
        Map<String, Object> newValue,
        String ip,
        String status,
        Long duration
) {
    public static AuditLogVO from(AuditLog entity) {
        return new AuditLogVO(
                entity.getId(),
                entity.getModule(),
                entity.getAction(),
                entity.getTargetId(),
                entity.getOldValue(),
                entity.getNewValue(),
                entity.getIp() == null ? null : entity.getIp().getHostAddress(),
                entity.getStatus().name(),
                entity.getDuration()
        );
    }
}
