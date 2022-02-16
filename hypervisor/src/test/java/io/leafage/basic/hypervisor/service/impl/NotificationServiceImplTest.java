package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.Notification;
import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.NotificationRepository;
import org.bson.types.ObjectId;
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
 * notification service test
 *
 * @author liwenqiang 2022/2/16 8:54
 **/
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void retrieve() {
        given(this.notificationRepository.findByReadAndEnabledTrue(PageRequest.of(0, 2), true))
                .willReturn(Flux.just(Mockito.mock(Notification.class)));

        StepVerifier.create(notificationService.retrieve(0, 2, true)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Notification notification = new Notification();
        notification.setId(new ObjectId());
        notification.setCode("32309FJK0");
        notification.setTitle("标题");
        notification.setContent("内容信息");
        notification.setRead(false);
        notification.setReceiver("test");
        given(this.notificationRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(notification));

        given(this.notificationRepository.save(Mockito.any(Notification.class))).willReturn(Mono.just(Mockito.mock(Notification.class)));

        StepVerifier.create(notificationService.fetch("32309FJK0")).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.notificationRepository.countByReadFalse()).willReturn(Mono.just(2L));

        StepVerifier.create(notificationService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Account account = new Account();
        account.setUsername("test");
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        given(this.notificationRepository.insert(Mockito.any(Notification.class))).willReturn(Mono.just(Mockito.mock(Notification.class)));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("标题");
        notificationDTO.setContent("内容信息");
        notificationDTO.setReceiver("test");
        StepVerifier.create(notificationService.create(notificationDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Notification notification = new Notification();
        notification.setId(new ObjectId());
        given(this.notificationRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(notification));

        given(this.notificationRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(notificationService.remove("32309FJK0")).verifyComplete();
    }
}