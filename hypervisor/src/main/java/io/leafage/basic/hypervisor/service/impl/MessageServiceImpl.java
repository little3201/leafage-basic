/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Group;
import io.leafage.basic.hypervisor.domain.Message;
import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.repository.MessageRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.MessageService;
import io.leafage.basic.hypervisor.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.NoSuchElementException;

/**
 * message service impl
 *
 * @author wq li
 */
@Service
public class MessageServiceImpl extends ReactiveAbstractTreeNodeService<Group> implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    /**
     * <p>Constructor for MessageServiceImpl.</p>
     *
     * @param messageRepository a {@link io.leafage.basic.hypervisor.repository.MessageRepository} object
     * @param userRepository    a {@link io.leafage.basic.hypervisor.repository.UserRepository} object
     */
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<MessageVO>> retrieve(int page, int size, String receiver) {
        Pageable pageable = PageRequest.of(page, size);
        Flux<MessageVO> voFlux = messageRepository.findByReceiver(receiver, pageable).flatMap(this::convertOuter);

        Mono<Long> count = messageRepository.countByReceiver(receiver);

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /** {@inheritDoc} */
    @Override
    public Mono<MessageVO> fetch(Long id) {
        Assert.notNull(id, "message id must not be null.");
        return messageRepository.findById(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(message -> message.setRead(Boolean.TRUE))
                .flatMap(messageRepository::save)
                .flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<MessageVO> create(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        return Mono.just(message).map(m -> {
            userRepository.getByUsername(messageDTO.getReceiver()).switchIfEmpty(Mono.error(NoSuchElementException::new))
                    .doOnNext(user -> message.setReceiver(user.getUsername()));
            return m;
        }).flatMap(messageRepository::save).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "message id must not be null.");
        return messageRepository.deleteById(id);
    }

    /**
     * 数据转换
     *
     * @param message 信息
     * @return NotificationVO 输出对象
     */
    private Mono<MessageVO> convertOuter(Message message) {
        return Mono.just(message).map(m -> {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(m, vo);
            vo.setLastModifiedDate(m.getLastModifiedDate().orElse(null));
            return vo;
        });

    }

}
