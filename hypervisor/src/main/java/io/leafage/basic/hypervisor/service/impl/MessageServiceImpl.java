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

import io.leafage.basic.hypervisor.domain.Message;
import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.repository.MessageRepository;
import io.leafage.basic.hypervisor.service.MessageService;
import io.leafage.basic.hypervisor.vo.MessageVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Optional;

/**
 * message service impl.
 *
 * @author wq li 2022/1/26 15:20
 **/
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Page<MessageVO> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findAll(pageable).map(this::convert);
    }

    @Override
    public MessageVO fetch(Long id) {
        Assert.notNull(id, "message id must not be null.");
        Message message = messageRepository.findById(id).orElse(null);
        return this.convert(message);
    }

    @Override
    public MessageVO create(MessageDTO dto) {
        Message message = new Message();
        BeanCopier copier = BeanCopier.create(MessageDTO.class, Message.class, false);
        copier.copy(dto, message, null);

        messageRepository.saveAndFlush(message);
        return this.convert(message);
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private MessageVO convert(Message message) {
        MessageVO vo = new MessageVO();
        BeanCopier copier = BeanCopier.create(Message.class, MessageVO.class, false);
        copier.copy(message, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = message.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);
        return vo;
    }
}
