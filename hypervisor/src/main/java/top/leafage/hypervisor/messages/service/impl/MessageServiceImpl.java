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
import org.springframework.util.CollectionUtils;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.messages.domain.Message;
import top.leafage.hypervisor.messages.domain.MessageInbox;
import top.leafage.hypervisor.messages.domain.dto.MessageDTO;
import top.leafage.hypervisor.messages.domain.vo.MessageVO;
import top.leafage.hypervisor.messages.repository.MessageInboxRepository;
import top.leafage.hypervisor.messages.repository.MessageRepository;
import top.leafage.hypervisor.messages.service.MessageService;
import top.leafage.hypervisor.system.domain.User;
import top.leafage.hypervisor.system.repository.UserRepository;

import java.util.List;

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
    private final UserRepository userRepository;

    /**
     * Constructor for MessageServiceImpl.
     *
     * @param messageRepository      a {@link MessageRepository} object
     * @param messageInboxRepository a {@link MessageInboxRepository} object
     * @param userRepository         a {@link UserRepository} object
     */
    public MessageServiceImpl(MessageRepository messageRepository, MessageInboxRepository messageInboxRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.messageInboxRepository = messageInboxRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MessageVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Message> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

        return messageRepository.findAll(spec, pageable)
                .map(MessageVO::from);
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
        Message entity = messageRepository.save(MessageDTO.toEntity(dto));
        return MessageVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MessageVO modify(Long id, MessageDTO dto) {
        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("message not found: " + id));
        if (!existing.getTitle().equals(dto.getTitle()) &&
                messageRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }
        copier.copy(dto, existing, null);
        if (existing.getStatus().equals(Message.Status.REVOKED)) {
            existing.setStatus(Message.Status.DRAFT);
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
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("message not found: " + id));
        List<String> receivers = message.getReceivers();
        if (CollectionUtils.isEmpty(receivers)) {
            receivers = userRepository.findAll(Example.of(new User())).stream().map(User::getUsername).toList();
        }

        List<MessageInbox> inboxList = receivers.stream().map(receiver -> new MessageInbox(message, receiver)).toList();
        messageInboxRepository.saveAll(inboxList);

        return messageRepository.updateStatusAndPublishedAtById(id, Message.Status.PUBLISHED) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean revoke(Long id) {
        messageInboxRepository.deleteByMessageId(id);

        return messageRepository.updateStatusAndPublishedAtNullById(id, Message.Status.REVOKED) > 0;
    }

}
