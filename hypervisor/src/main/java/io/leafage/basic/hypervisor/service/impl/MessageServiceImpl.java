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
