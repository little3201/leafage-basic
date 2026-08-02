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

package top.leafage.hypervisor.system.domain.vo;


import top.leafage.hypervisor.system.domain.User;

import java.util.List;

/**
 * vo class for user.
 *
 * @author wq li
 */
public record UserVO(
        Long id,
        String username,
        String fullName,
        String email,
        List<RoleVO> roles,
        boolean enabled
) {
    public static UserVO from(User entity) {
        return from(entity, true);
    }

    public static UserVO from(User entity, List<RoleVO> roles) {
        return new UserVO(
                entity.getId(),
                entity.getUsername(),
                entity.getFullName(),
                null,
                roles,
                entity.isEnabled()
        );
    }

    public static UserVO from(User entity, boolean maskEmail) {
        return new UserVO(
                entity.getId(),
                entity.getUsername(),
                entity.getFullName(),
                mask(entity.getEmail(), maskEmail),
                entity.getRoles().stream().map(RoleVO::from).toList(),
                entity.isEnabled()
        );
    }

    private static String mask(String email, boolean mask) {
        if (email == null || email.isEmpty()) {
            return "";
        } else if (!mask) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 3) {
            return email; // 邮箱太短不脱敏
        }

        String prefix = email.substring(0, 3);
        String suffix = email.substring(atIndex);
        int starCount = atIndex - 3;

        return prefix + "*".repeat(starCount) + suffix;
    }
}
