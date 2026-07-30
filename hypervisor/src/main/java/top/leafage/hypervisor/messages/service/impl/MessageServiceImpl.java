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
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.messages.domain.Message;
import top.leafage.hypervisor.messages.domain.MessageInbox;
import top.leafage.hypervisor.messages.domain.MessageTarget;
import top.leafage.hypervisor.messages.domain.dto.MessageDTO;
import top.leafage.hypervisor.messages.domain.vo.MessageVO;
import top.leafage.hypervisor.messages.domain.vo.TargetVO;
import top.leafage.hypervisor.messages.repository.MessageInboxRepository;
import top.leafage.hypervisor.messages.repository.MessageRepository;
import top.leafage.hypervisor.messages.repository.MessageTargetRepository;
import top.leafage.hypervisor.messages.service.MessageService;
import top.leafage.hypervisor.system.domain.Group;
import top.leafage.hypervisor.system.domain.Role;
import top.leafage.hypervisor.system.domain.User;
import top.leafage.hypervisor.system.repository.GroupRepository;
import top.leafage.hypervisor.system.repository.RoleRepository;
import top.leafage.hypervisor.system.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;

/**
 * message service impl.
 *
 * @author wq li
 */
@OperationLog("messages")
@Service
public class MessageServiceImpl implements MessageService {

    private static final BeanCopier copier = BeanCopier.create(MessageDTO.class, Message.class, false);

    private final MessageRepository messageRepository;
    private final MessageInboxRepository messageInboxRepository;
    private final MessageTargetRepository messageTargetRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;

    /**
     * Constructor for MessageServiceImpl.
     *
     * @param messageRepository       a {@link MessageRepository} object
     * @param messageInboxRepository  a {@link MessageInboxRepository} object
     * @param messageTargetRepository a {@link MessageTargetRepository} object
     * @param userRepository          a {@link UserRepository} object
     * @param groupRepository         a {@link GroupRepository} object
     * @param roleRepository          a {@link RoleRepository} object
     */
    public MessageServiceImpl(MessageRepository messageRepository, MessageInboxRepository messageInboxRepository, MessageTargetRepository messageTargetRepository,
                              UserRepository userRepository, GroupRepository groupRepository, RoleRepository roleRepository) {
        this.messageRepository = messageRepository;
        this.messageInboxRepository = messageInboxRepository;
        this.messageTargetRepository = messageTargetRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MessageVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Message> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

        Page<Message> messagePage = messageRepository.findAll(spec, pageable);
        List<Long> ids = messagePage.getContent()
                .stream()
                .map(Message::getId)
                .toList();

        List<MessageTarget> targets =
                messageTargetRepository.findByMessageIdIn(ids);

        Map<Long, List<MessageTarget>> targetMap =
                targets.stream()
                        .collect(Collectors.groupingBy(
                                t -> t.getMessage().getId()
                        ));

        Map<Long, String> users = resolveTargetUsers(targets);
        Map<Long, String> groups = resolveTargetGroups(targets);
        Map<Long, String> roles = resolveTargetRoles(targets);

        return messagePage.map(message ->
                MessageVO.from(message, resolveTargets(targetMap.getOrDefault(message.getId(), List.of()), users, groups, roles))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return messageRepository.findById(id)
                .map(MessageVO::from)
                .orElseThrow(() -> new EntityNotFoundException("message not found: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MessageVO create(MessageDTO dto) {
        if (messageRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }

        Message message = MessageDTO.toEntity(dto);
        if (dto.getScope() != Message.Scope.ALL) {
            MessageTarget.TargetType targetType =
                    switch (dto.getScope()) {
                        case USER -> MessageTarget.TargetType.USER;
                        case GROUP -> MessageTarget.TargetType.GROUP;
                        case ROLE -> MessageTarget.TargetType.ROLE;
                        default -> throw new IllegalStateException();
                    };

            dto.getTargets()
                    .forEach(id ->
                            message.addTarget(targetType, id)
                    );
        }

        Message entity = messageRepository.save(message);
        return MessageVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MessageVO modify(Long id, MessageDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("message not found: " + id));
        if (!existing.getTitle().equals(dto.getTitle()) &&
                messageRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }
        copier.copy(dto, existing, null);
        // 撤销后编辑，更新状态为 DRAFT
        if (existing.getStatus().equals(Message.Status.REVOKED)) {
            existing.setStatus(Message.Status.DRAFT);
        }

        existing.clearTargets();
        if (dto.getScope() != Message.Scope.ALL) {
            MessageTarget.TargetType type =
                    MessageTarget.TargetType.valueOf(
                            dto.getScope().name()
                    );

            dto.getTargets()
                    .forEach(targetId ->
                            existing.addTarget(type, targetId)
                    );
        }
        Message entity = messageRepository.save(existing);
        return MessageVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!messageRepository.existsById(id)) {
            throw new EntityNotFoundException("message not found: " + id);
        }
        messageRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean publish(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("message not found: " + id));
        if (message.getStatus() != Message.Status.DRAFT) {
            throw new IllegalStateException("message status is not draft, can not be published.");
        }

        int published = messageRepository.updateStatusAndPublishedAtById(id, Message.Status.PUBLISHED);
        if (published > 0) {
            List<User> receivers = resolveReceivers(message);

            List<MessageInbox> inboxList = receivers.stream().map(receiver -> new MessageInbox(message, receiver)).toList();
            messageInboxRepository.saveAll(inboxList);
        }

        return published > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean revoke(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        int revoked = messageRepository.updateStatusAndPublishedAtNullById(id, Message.Status.REVOKED);
        if (revoked > 0) {
            messageInboxRepository.deleteByMessageId(id);
        }

        return revoked > 0;
    }

    private List<TargetVO> resolveTargets(List<MessageTarget> targets, Map<Long, String> userNames,
                                          Map<Long, String> groupNames, Map<Long, String> roleNames) {
        return targets.stream()
                .map(target -> {
                    String name = switch (target.getType()) {
                        case USER -> userNames.get(target.getTargetId());
                        case GROUP -> groupNames.get(target.getTargetId());
                        case ROLE -> roleNames.get(target.getTargetId());
                    };

                    return new TargetVO(target.getTargetId(), name);
                })
                .toList();
    }

    private Map<Long, String> resolveTargetUsers(List<MessageTarget> targets) {
        List<Long> ids = targets.stream()
                .filter(t -> t.getType() == MessageTarget.TargetType.USER)
                .map(MessageTarget::getTargetId)
                .distinct()
                .toList();

        return userRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        User::getId,
                        User::getFullName
                ));
    }

    private Map<Long, String> resolveTargetGroups(List<MessageTarget> targets) {
        List<Long> ids = targets.stream()
                .filter(t -> t.getType() == MessageTarget.TargetType.GROUP)
                .map(MessageTarget::getTargetId)
                .distinct()
                .toList();

        return groupRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        Group::getId,
                        Group::getName
                ));
    }

    private Map<Long, String> resolveTargetRoles(List<MessageTarget> targets) {
        List<Long> ids = targets.stream()
                .filter(t -> t.getType() == MessageTarget.TargetType.ROLE)
                .map(MessageTarget::getTargetId)
                .distinct()
                .toList();

        return roleRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        Role::getId,
                        Role::getName
                ));
    }

    private List<User> resolveReceivers(Message message) {
        return switch (message.getScope()) {
            case ALL -> userRepository.findAll(Example.of(new User()));
            case USER -> resolveUsers(message);
            case GROUP -> resolveGroups(message);
            case ROLE -> resolveRoles(message);
        };
    }

    private List<User> resolveUsers(Message message) {
        List<Long> ids =
                message.getTargets()
                        .stream()
                        .map(MessageTarget::getTargetId)
                        .toList();

        return userRepository.findAllById(ids);
    }

    private List<User> resolveGroups(Message message) {
        List<Long> groupIds =
                message.getTargets()
                        .stream()
                        .map(MessageTarget::getTargetId)
                        .toList();

        return groupRepository.findWithMembersByIdIn(groupIds)
                .stream().flatMap(group -> group.getMembers().stream())
                .distinct()
                .toList();
    }

    private List<User> resolveRoles(Message message) {
        List<Long> roleIds =
                message.getTargets()
                        .stream()
                        .map(MessageTarget::getTargetId)
                        .toList();

        return userRepository.findByRolesIdIn(roleIds);
    }
}
