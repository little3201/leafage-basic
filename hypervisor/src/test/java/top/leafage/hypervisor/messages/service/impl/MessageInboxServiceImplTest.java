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

package top.leafage.hypervisor.messages.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import top.leafage.hypervisor.messages.domain.Message;
import top.leafage.hypervisor.messages.domain.MessageInbox;
import top.leafage.hypervisor.messages.domain.vo.MessageInboxVO;
import top.leafage.hypervisor.messages.repository.MessageInboxRepository;
import top.leafage.hypervisor.system.domain.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageInboxServiceImplTest {

    @Mock
    private MessageInboxRepository messageInboxRepository;

    @InjectMocks
    private MessageInboxServiceImpl messageInboxService;

    private MessageInbox inbox;

    @BeforeEach
    void setUp() {
        Message message = new Message("test", "body", "notice", Message.Scope.ALL);
        ReflectionTestUtils.setField(message, "id", 1L);
        User receiver = new User("test", "password", "test", "test@example.com");
        inbox = new MessageInbox(message, receiver);
        ReflectionTestUtils.setField(inbox, "id", 2L);
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("test", "password"));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void retrieve() {
        Page<MessageInbox> page = new PageImpl<>(List.of(inbox));
        when(messageInboxRepository.findAll(ArgumentMatchers.<Specification<MessageInbox>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<MessageInboxVO> voPage = messageInboxService.retrieve(0, 2, "id", true, "status:eq:UNREAD");
        assertEquals(1, voPage.getTotalElements());
        assertEquals(MessageInbox.Status.UNREAD, voPage.getContent().getFirst().status());
        verify(messageInboxRepository).findAll(ArgumentMatchers.<Specification<MessageInbox>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
        when(messageInboxRepository.findById(anyLong())).thenReturn(Optional.of(inbox));

        MessageInboxVO vo = messageInboxService.fetch(2L);
        assertEquals(2L, vo.id());
        assertEquals("test", vo.message().title());
    }

    @Test
    void fetch_not_found() {
        when(messageInboxRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageInboxService.fetch(2L));
        assertEquals("message not found: 2", exception.getMessage());
    }

    @Test
    void remove() {
        when(messageInboxRepository.existsById(anyLong())).thenReturn(true);

        messageInboxService.remove(2L);
        verify(messageInboxRepository).deleteById(2L);
    }

    @Test
    void remove_not_found() {
        when(messageInboxRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageInboxService.remove(2L));
        assertEquals("message not found: 2", exception.getMessage());
    }

    @Test
    void read() {
        when(messageInboxRepository.updateStatusAndReadAtById(2L)).thenReturn(1);

        assertTrue(messageInboxService.read(2L));
    }

    @Test
    void readAll() {
        when(messageInboxRepository.findAllByStatus(MessageInbox.Status.UNREAD)).thenReturn(List.of(inbox));
        when(messageInboxRepository.updateStatusAndReadAtByIds(List.of(2L))).thenReturn(1);

        assertTrue(messageInboxService.readAll());
    }

    @Test
    void clear() {
        when(messageInboxRepository.findAllInbox()).thenReturn(List.of(inbox));

        messageInboxService.clear();
        verify(messageInboxRepository).deleteAllById(List.of(2L));
    }
}
