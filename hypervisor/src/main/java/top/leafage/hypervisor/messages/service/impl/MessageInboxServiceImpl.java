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
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.messages.domain.MessageInbox;
import top.leafage.hypervisor.messages.domain.vo.MessageInboxVO;
import top.leafage.hypervisor.messages.repository.MessageInboxRepository;
import top.leafage.hypervisor.messages.service.MessageInboxService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;

/**
 * message service impl.
 *
 * @author wq li
 */
@OperationLog("messages")
@Service
public class MessageInboxServiceImpl implements MessageInboxService {

    private final MessageInboxRepository messageInboxRepository;

    /**
     * Constructor for MessageServiceImpl.
     *
     * @param messageInboxRepository a {@link MessageInboxRepository} object
     */
    public MessageInboxServiceImpl(MessageInboxRepository messageInboxRepository) {
        this.messageInboxRepository = messageInboxRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MessageInboxVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<MessageInbox> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            // receiver
            Predicate receiverPredicate = cb.equal(
                    root.get("receiver").get("username"),
                    Objects.requireNonNull(SecurityContextHolder
                                    .getContext()
                                    .getAuthentication())
                            .getName()
            );

            return cb.and(basePredicate, receiverPredicate);
        };

        return messageInboxRepository.findAll(spec, pageable)
                .map(MessageInboxVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageInboxVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return messageInboxRepository.findById(id)
                .map(MessageInboxVO::from)
                .orElseThrow(() -> new EntityNotFoundException("message not found: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!messageInboxRepository.existsById(id)) {
            throw new EntityNotFoundException("message not found: " + id);
        }
        messageInboxRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean read(Long id) {
        return messageInboxRepository.updateStatusAndReadAtById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean readAll() {
        List<Long> ids = messageInboxRepository.findAllByStatus(MessageInbox.Status.UNREAD)
                .stream().map(MessageInbox::getId)
                .toList();
        return messageInboxRepository.updateStatusAndReadAtByIds(ids) > 0;
    }

    @Override
    public void clear() {
        List<Long> ids = messageInboxRepository.findAllInbox()
                .stream().map(MessageInbox::getId)
                .toList();
        messageInboxRepository.deleteAllById(ids);
    }
}
