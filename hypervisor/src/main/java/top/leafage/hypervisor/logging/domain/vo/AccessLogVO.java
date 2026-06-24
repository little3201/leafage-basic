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

package top.leafage.hypervisor.logging.domain.vo;

import top.leafage.hypervisor.logging.domain.AccessLog;

import java.util.Map;

/**
 * vo class for access log.
 *
 * @author wq li
 */
public record AccessLogVO(
        Long id,
        String url,
        String httpMethod,
        Long targetId,
        String ip,
        Map<String, Object> params,
        Map<String, Object> body,
        Integer statusCode,
        Long duration,
        Map<String, Object> response
) {
    public static AccessLogVO from(AccessLog entity) {
        return new AccessLogVO(
                entity.getId(),
                entity.getUrl(),
                entity.getHttpMethod(),
                entity.getTargetId(),
                entity.getIp() == null ? null : entity.getIp().getHostAddress(),
                entity.getParams(),
                entity.getBody(),
                entity.getStatusCode(),
                entity.getDuration(),
                entity.getResponse()
        );
    }
}
