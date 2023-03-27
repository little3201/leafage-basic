/*
 *  Copyright 2018-2023 the original author or authors.
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.NoSuchElementException;

/**
 * notification service impl
 *
 * @author liwenqiang 2022-02-10 13:49
 */
@Service
public class MessageServiceImpl extends ReactiveAbstractTreeNodeService<Group> implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Page<MessageVO>> retrieve(int page, int size, boolean read) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<MessageVO> voFlux = messageRepository.findByRead(read, pageRequest).map(this::convertOuter);

        Mono<Long> count = messageRepository.countByRead(read);

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<MessageVO> fetch(Long id) {
        Assert.notNull(id, "notification id must not be null.");
        return messageRepository.findById(id).doOnNext(message ->
                message.setRead(true)).flatMap(messageRepository::save).map(this::convertOuter);
    }

    @Override
    public Mono<MessageVO> create(MessageDTO messageDTO) {
        return userRepository.getByUsername(messageDTO.getReceiver())
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(user -> Mono.just(messageDTO).map(dto -> {
                            Message message = new Message();
                            BeanUtils.copyProperties(messageDTO, message);
                            message.setReceiver(user.getUsername());
                            return message;
                        })
                        .flatMap(messageRepository::save).map(this::convertOuter))
                .switchIfEmpty(Mono.error(new NoSuchElementException()));
    }

    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "notification id must not be null.");
        return messageRepository.deleteById(id);
    }

    /**
     * 数据转换
     *
     * @param message 信息
     * @return NotificationVO 输出对象
     */
    private MessageVO convertOuter(Message message) {
        MessageVO outer = new MessageVO();
        BeanUtils.copyProperties(message, outer);
        return outer;
    }

}
