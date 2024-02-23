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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;

/**
 * message service impl.
 *
 * @author wq li 2022/1/26 15:20
 **/
@Service
public class MessageServiceImpl extends ServletAbstractTreeNodeService<Message> implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Page<MessageVO> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public MessageVO fetch(Long id) {
        Assert.notNull(id, "message id must not be null.");
        Message message = messageRepository.findById(id).orElse(null);
        return this.convertOuter(message);
    }

    @Override
    public MessageVO create(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        messageRepository.saveAndFlush(message);
        return this.convertOuter(message);
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private MessageVO convertOuter(Message message) {
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(message, messageVO);
        return messageVO;
    }
}
