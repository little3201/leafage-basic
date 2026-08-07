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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import top.leafage.hypervisor.messages.domain.Message;
import top.leafage.hypervisor.messages.domain.MessageTarget;
import top.leafage.hypervisor.messages.domain.dto.MessageDTO;
import top.leafage.hypervisor.messages.domain.vo.MessageVO;
import top.leafage.hypervisor.system.domain.Group;
import top.leafage.hypervisor.system.domain.Role;
import top.leafage.hypervisor.system.domain.User;
import top.leafage.hypervisor.messages.repository.MessageInboxRepository;
import top.leafage.hypervisor.messages.repository.MessageRepository;
import top.leafage.hypervisor.messages.repository.MessageTargetRepository;
import top.leafage.hypervisor.system.repository.GroupRepository;
import top.leafage.hypervisor.system.repository.RoleRepository;
import top.leafage.hypervisor.system.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/**
 * Message service test.
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageInboxRepository messageInboxRepository;

    @Mock
    private MessageTargetRepository messageTargetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private MessageDTO dto;
    private Message entity;

    @BeforeEach
    void setUp() {
        dto = new MessageDTO();
        dto.setTitle("test");
        dto.setBody("body");
        dto.setType("notice");
        dto.setScope(Message.Scope.ALL);
        dto.setTargets(List.of());

        entity = MessageDTO.toEntity(dto);
    }

    @Test
    void retrieve() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        User user = new User("test", "password", "full name", "test@example.com");
        ReflectionTestUtils.setField(user, "id", 2L);
        Group group = new Group("group", null);
        ReflectionTestUtils.setField(group, "id", 3L);
        Role role = new Role("role", "ROLE");
        ReflectionTestUtils.setField(role, "id", 4L);
        List<MessageTarget> targets = List.of(
                new MessageTarget(entity, MessageTarget.TargetType.USER, 2L),
                new MessageTarget(entity, MessageTarget.TargetType.GROUP, 3L),
                new MessageTarget(entity, MessageTarget.TargetType.ROLE, 4L)
        );
        Page<Message> page = new PageImpl<>(List.of(entity));

        when(messageRepository.findAll(ArgumentMatchers.<Specification<Message>>any(),
                any(Pageable.class))).thenReturn(page);
        when(messageTargetRepository.findByMessageIdIn(anyList())).thenReturn(targets);
        when(userRepository.findAllById(List.of(2L))).thenReturn(List.of(user));
        when(groupRepository.findAllById(List.of(3L))).thenReturn(List.of(group));
        when(roleRepository.findAllById(List.of(4L))).thenReturn(List.of(role));

        Page<MessageVO> voPage = messageService.retrieve(0, 2, "id", true, "test");
        assertEquals(1, voPage.getTotalElements());
        assertEquals(1, voPage.getContent().size());
        assertEquals(3, voPage.getContent().getFirst().targets().size());
        verify(messageRepository).findAll(ArgumentMatchers.<Specification<Message>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
        when(messageRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        MessageVO vo = messageService.fetch(anyLong());
        assertNotNull(vo);
        assertEquals("test", vo.title());
        verify(messageRepository).findById(anyLong());
    }

    @Test
    void fetch_not_found() {
        when(messageRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> messageService.fetch(anyLong())
        );
        assertEquals("message not found: 0", exception.getMessage());
        verify(messageRepository).findById(anyLong());
    }

    @Test
    void create() {
        when(messageRepository.existsByTitle("test")).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenReturn(entity);

        MessageVO vo = messageService.create(dto);
        assertNotNull(vo);
        assertEquals("test", vo.title());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void create_with_user_targets() {
        dto.setScope(Message.Scope.USER);
        dto.setTargets(List.of(2L));
        when(messageRepository.existsByTitle("test")).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageVO vo = messageService.create(dto);

        assertEquals(Message.Scope.USER, vo.scope());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void create_name_conflict() {
        when(messageRepository.existsByTitle("test")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> messageService.create(dto)
        );
        assertEquals("title already exists: test", exception.getMessage());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void modify() {
        when(messageRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(messageRepository.existsByTitle("demo")).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenReturn(entity);

        dto.setTitle("demo");
        MessageVO vo = messageService.modify(1L, dto);
        assertNotNull(vo);
        assertEquals("demo", vo.title());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void modify_revoked_message_with_role_targets() {
        entity.setStatus(Message.Status.REVOKED);
        dto.setScope(Message.Scope.ROLE);
        dto.setTargets(List.of(4L));
        when(messageRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageVO vo = messageService.modify(1L, dto);

        assertEquals(Message.Status.DRAFT, vo.status());
        assertEquals(1, entity.getTargets().size());
    }

    @Test
    void modify_not_found() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageService.modify(1L, dto));
        assertEquals("message not found: 1", exception.getMessage());
    }

    @Test
    void modify_username_conflict() {
        when(messageRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(messageRepository.existsByTitle("demo")).thenReturn(true);

        dto.setTitle("demo");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> messageService.modify(1L, dto)
        );
        assertEquals("title already exists: demo", exception.getMessage());
    }

    @Test
    void remove() {
        when(messageRepository.existsById(anyLong())).thenReturn(true);
        messageService.remove(1L);

        verify(messageRepository).deleteById(anyLong());
    }

    @Test
    void remove_not_found() {
        when(messageRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> messageService.remove(anyLong())
        );
        assertEquals("message not found: 0", exception.getMessage());
    }

    @Test
    void publish() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        User receiver = new User("test", "password", "test", "test@example.com");
        when(messageRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepository.updateStatusAndPublishedAtById(1L, Message.Status.PUBLISHED)).thenReturn(1);
        when(userRepository.findAll(ArgumentMatchers.<Example<User>>any())).thenReturn(List.of(receiver));

        assertTrue(messageService.publish(1L));
        verify(messageInboxRepository).saveAll(anyList());
    }

    @Test
    void publish_user_scope() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        entity.setScope(Message.Scope.USER);
        entity.addTarget(MessageTarget.TargetType.USER, 2L);
        User receiver = new User("test", "password", "test", "test@example.com");
        when(messageRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepository.updateStatusAndPublishedAtById(1L, Message.Status.PUBLISHED)).thenReturn(1);
        when(userRepository.findAllById(List.of(2L))).thenReturn(List.of(receiver));

        assertTrue(messageService.publish(1L));
        verify(messageInboxRepository).saveAll(anyList());
    }

    @Test
    void publish_group_scope() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        entity.setScope(Message.Scope.GROUP);
        entity.addTarget(MessageTarget.TargetType.GROUP, 3L);
        User receiver = new User("test", "password", "test", "test@example.com");
        Group group = new Group("group", null);
        group.addMember(receiver);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepository.updateStatusAndPublishedAtById(1L, Message.Status.PUBLISHED)).thenReturn(1);
        when(groupRepository.findWithMembersByIdIn(List.of(3L))).thenReturn(List.of(group));

        assertTrue(messageService.publish(1L));
        verify(messageInboxRepository).saveAll(anyList());
    }

    @Test
    void publish_role_scope() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        entity.setScope(Message.Scope.ROLE);
        entity.addTarget(MessageTarget.TargetType.ROLE, 4L);
        User receiver = new User("test", "password", "test", "test@example.com");
        when(messageRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepository.updateStatusAndPublishedAtById(1L, Message.Status.PUBLISHED)).thenReturn(1);
        when(userRepository.findByRolesIdIn(List.of(4L))).thenReturn(List.of(receiver));

        assertTrue(messageService.publish(1L));
        verify(messageInboxRepository).saveAll(anyList());
    }

    @Test
    void publish_not_found() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> messageService.publish(1L)
        );
        assertEquals("message not found: 1", exception.getMessage());
    }

    @Test
    void publish_status_not_draft() {
        entity.setStatus(Message.Status.PUBLISHED);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(entity));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> messageService.publish(1L)
        );
        assertEquals("message status is not draft, can not be published.", exception.getMessage());
    }

    @Test
    void revoke() {
        when(messageRepository.updateStatusAndPublishedAtNullById(1L, Message.Status.REVOKED)).thenReturn(1);

        assertTrue(messageService.revoke(1L));
        verify(messageInboxRepository).deleteByMessageId(1L);
    }

    @Test
    void revoke_not_updated() {
        when(messageRepository.updateStatusAndPublishedAtNullById(1L, Message.Status.REVOKED)).thenReturn(0);

        assertFalse(messageService.revoke(1L));
        verify(messageInboxRepository, never()).deleteByMessageId(1L);
    }
}
