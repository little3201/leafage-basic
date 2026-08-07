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

package top.leafage.hypervisor.audits.controller;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import top.leafage.hypervisor.audits.service.PatrolService;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WithMockUser
@WebMvcTest(PatrolController.class)
class PatrolControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private PatrolService patrolService;

    @MockitoBean
    private UserService userService;

    private UserVO vo;

    @BeforeEach
    void setUp() {
        vo = new UserVO(1L, "test", "test", "test@example.com", List.of(), true);
    }

    @Test
    void retrieve() {
        Page<UserVO> page = new PageImpl<>(List.of(vo), mock(PageRequest.class), 1L);
        when(patrolService.retrieve(anyInt(), anyInt(), anyString(), anyBoolean(), anyString())).thenReturn(page);

        assertThat(mvc.get().uri("/audit-patrols")
                .queryParam("page", "0")
                .queryParam("size", "2")
                .queryParam("sortBy", "id")
                .queryParam("descending", "false")
                .queryParam("filters", "username:like:test"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.content")
                .convertTo(InstanceOfAssertFactories.list(UserVO.class))
                .hasSize(1)
                .element(0).satisfies(vo -> assertThat(vo.username()).isEqualTo("test"));
    }

    @Test
    void fetch() {
        when(userService.fetch(anyLong())).thenReturn(vo);

        assertThat(mvc.get().uri("/audit-patrols/{id}", 1L))
                .hasStatusOk()
                .bodyJson()
                .convertTo(UserVO.class)
                .satisfies(vo -> assertThat(vo.username()).isEqualTo("test"));
    }

    @Test
    void enable() {
        when(userService.enable(anyLong())).thenReturn(true);

        assertThat(mvc.patch().uri("/audit-patrols/{id}/enable", 1L).with(csrf().asHeader()))
                .hasStatusOk();
    }

    @Test
    void disable() {
        when(userService.disable(anyLong())).thenReturn(true);

        assertThat(mvc.patch().uri("/audit-patrols/{id}/disable", 1L).with(csrf().asHeader()))
                .hasStatusOk();
    }

    @Test
    void remove() {
        userService.remove(anyLong());

        assertThat(mvc.delete().uri("/audit-patrols/{id}", 1L).with(csrf().asHeader()))
                .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void remove_error() {
        doThrow(new RuntimeException()).when(userService).remove(anyLong());

        assertThat(mvc.delete().uri("/audit-patrols/{id}", 1L).with(csrf().asHeader()))
                .hasStatus5xxServerError();
    }
}
