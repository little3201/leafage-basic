/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.domain.Message;
import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.repository.MessageRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * message service test
 *
 * @author liwenqiang 2022-02-16 8:54
 **/
@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private MessageDTO messageDTO;

    @BeforeEach
    void init() {
        messageDTO = new MessageDTO();
        messageDTO.setTitle("标题");
        messageDTO.setContext("内容信息");
        messageDTO.setReceiver("test");
    }

    @Test
    void retrieve() {
        given(this.messageRepository.findByReceiver(Mockito.anyString(), Mockito.any(PageRequest.class)))
                .willReturn(Flux.just(Mockito.mock(Message.class)));

        given(this.messageRepository.countByReceiver(Mockito.anyString())).willReturn(Mono.just(2L));

        StepVerifier.create(messageService.retrieve(0, 2, "test")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.messageRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Message.class)));

        given(this.messageRepository.save(Mockito.any(Message.class))).willReturn(Mono.just(Mockito.mock(Message.class)));

        StepVerifier.create(messageService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(User.class)));

        given(this.messageRepository.save(Mockito.any(Message.class))).willReturn(Mono.just(Mockito.mock(Message.class)));

        StepVerifier.create(messageService.create(messageDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        given(this.messageRepository.deleteById(Mockito.anyLong())).willReturn(Mono.empty());

        StepVerifier.create(messageService.remove(1L)).verifyComplete();
    }
}