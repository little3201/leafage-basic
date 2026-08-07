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

package top.leafage.hypervisor.audits.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import top.leafage.hypervisor.system.domain.User;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PatrolServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PatrolServiceImpl patrolService;

    @Test
    void retrieve() {
        User user = new User("test", "password", "test@example.com", "test");
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class))).thenReturn(page);

        Page<UserVO> voPage = patrolService.retrieve(0, 2, "id", true, "username:like:test");
        assertEquals(1, voPage.getTotalElements());
        assertEquals("test", voPage.getContent().getFirst().username());
        verify(userRepository).findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class));
    }
}
