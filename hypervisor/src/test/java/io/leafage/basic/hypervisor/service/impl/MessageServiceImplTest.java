package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Message;
import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.repository.MessageRepository;
import io.leafage.basic.hypervisor.vo.MessageVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * message service test
 *
 * @author liwenqiang 2022/3/3 11:25
 **/
@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;


    @Test
    void retrieve() {
        Page<Message> page = new PageImpl<>(List.of(Mockito.mock(Message.class)));
        given(this.messageRepository.findAll(PageRequest.of(0, 2))).willReturn(page);

        Page<MessageVO> voPage = messageService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.messageRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Message.class)));

        MessageVO messageVO = messageService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(messageVO);
    }


    @Test
    void create() {
        given(this.messageRepository.saveAndFlush(Mockito.any(Message.class))).willReturn(Mockito.mock(Message.class));

        MessageVO messageVO = messageService.create(Mockito.mock(MessageDTO.class));

        verify(this.messageRepository, times(1)).saveAndFlush(Mockito.any(Message.class));
        Assertions.assertNotNull(messageVO);
    }
}