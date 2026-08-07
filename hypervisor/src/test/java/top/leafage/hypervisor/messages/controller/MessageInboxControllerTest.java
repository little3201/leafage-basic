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

package top.leafage.hypervisor.messages.controller;

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
import top.leafage.hypervisor.messages.domain.Message;
import top.leafage.hypervisor.messages.domain.MessageInbox;
import top.leafage.hypervisor.messages.domain.vo.MessageInboxVO;
import top.leafage.hypervisor.messages.domain.vo.MessageVO;
import top.leafage.hypervisor.messages.service.MessageInboxService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WithMockUser
@WebMvcTest(MessageInboxController.class)
class MessageInboxControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private MessageInboxService messageInboxService;

    private MessageInboxVO vo;

    @BeforeEach
    void setUp() {
        MessageVO message = new MessageVO(1L, "test", "body", "notice", "sender", Message.Scope.ALL,
                List.of(), Message.Status.PUBLISHED, null);
        vo = new MessageInboxVO(2L, message, MessageInbox.Status.UNREAD, null);
    }

    @Test
    void retrieve() {
        Page<MessageInboxVO> page = new PageImpl<>(List.of(vo), mock(PageRequest.class), 1L);
        when(messageInboxService.retrieve(anyInt(), anyInt(), anyString(), anyBoolean(), anyString())).thenReturn(page);

        assertThat(mvc.get().uri("/message-inbox")
                .queryParam("page", "0")
                .queryParam("size", "2")
                .queryParam("sortBy", "id")
                .queryParam("descending", "false")
                .queryParam("filters", "status:eq:UNREAD"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.content")
                .convertTo(InstanceOfAssertFactories.list(MessageInboxVO.class))
                .hasSize(1);
    }

    @Test
    void fetch() {
        when(messageInboxService.fetch(anyLong())).thenReturn(vo);

        assertThat(mvc.get().uri("/message-inbox/{id}", 2L))
                .hasStatusOk()
                .bodyJson()
                .convertTo(MessageInboxVO.class)
                .satisfies(vo -> assertThat(vo.status()).isEqualTo(MessageInbox.Status.UNREAD));
    }

    @Test
    void read() {
        when(messageInboxService.read(anyLong())).thenReturn(true);

        assertThat(mvc.patch().uri("/message-inbox/{id}", 2L).with(csrf().asHeader()))
                .hasStatus(HttpStatus.ACCEPTED);
    }

    @Test
    void readAll() {
        when(messageInboxService.readAll()).thenReturn(true);

        assertThat(mvc.patch().uri("/message-inbox/read").with(csrf().asHeader()))
                .hasStatus(HttpStatus.ACCEPTED);
    }

    @Test
    void remove() {
        messageInboxService.remove(anyLong());

        assertThat(mvc.delete().uri("/message-inbox/{id}", 2L).with(csrf().asHeader()))
                .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void remove_error() {
        doThrow(new RuntimeException()).when(messageInboxService).remove(anyLong());

        assertThat(mvc.delete().uri("/message-inbox/{id}", 2L).with(csrf().asHeader()))
                .hasStatus5xxServerError();
    }

    @Test
    void clear() {
        messageInboxService.clear();

        assertThat(mvc.delete().uri("/message-inbox/clear").with(csrf().asHeader()))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}
