package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.entity.Notification;
import io.leafage.basic.hypervisor.repository.NotificationRepository;
import io.leafage.basic.hypervisor.vo.NotificationVO;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * notification service test
 *
 * @author liwenqiang 2022/3/3 11:25
 **/
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;


    @Test
    void retrieve() {
        Notification notification = new Notification();
        notification.setCode("213ADJ09");
        notification.setContent("测试内容");
        notification.setReceiver("test");
        notification.setRead(false);
        Page<Notification> regions = new PageImpl<>(List.of(notification));
        given(this.notificationRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(regions);

        Page<NotificationVO> voPage = notificationService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.notificationRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Notification.class));

        NotificationVO notificationVO = notificationService.fetch("213ADJ09");

        Assertions.assertNotNull(notificationVO);
    }


    @Test
    void create() {
        given(this.notificationRepository.save(Mockito.any(Notification.class))).willReturn(Mockito.mock(Notification.class));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setReceiver("test");
        notificationDTO.setContent("测试内容");
        notificationDTO.setRead(false);
        NotificationVO notificationVO = notificationService.create(notificationDTO);

        verify(this.notificationRepository, times(1)).save(Mockito.any(Notification.class));
        Assertions.assertNotNull(notificationVO);
    }
}