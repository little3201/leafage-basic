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

package top.leafage.hypervisor.messages.domain.vo;

import top.leafage.hypervisor.messages.domain.Message;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * vo class for message.
 *
 * @author wq li
 */
public record MessageVO(
        Long id,
        String title,
        String body,
        String type,
        String sender,
        Message.Scope scope,
        List<TargetVO> targets,
        Message.Status status,
        LocalDateTime publishedAt
) {
    public static MessageVO from(Message entity) {
        return MessageVO.from(entity, Collections.emptyList());
    }

    public static MessageVO from(Message entity, List<TargetVO> targets) {
        return new MessageVO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getType(),
                entity.getCreatedBy().orElse(null),
                entity.getScope(),
                targets,
                entity.getStatus(),
                entity.getPublishedAt()
        );
    }
}
