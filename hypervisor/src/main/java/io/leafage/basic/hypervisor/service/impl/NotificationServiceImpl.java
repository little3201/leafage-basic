package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.document.Notification;
import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.NotificationRepository;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;
import java.util.NoSuchElementException;

/**
 * notification service impl
 *
 * @author liwenqiang 2022-02-10 13:49
 */
@Service
public class NotificationServiceImpl extends ReactiveAbstractTreeNodeService<Group> implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, AccountRepository accountRepository) {
        this.notificationRepository = notificationRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Page<NotificationVO>> retrieve(int page, int size, boolean read) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<NotificationVO> voFlux = notificationRepository.findByReadAndEnabledTrue(read, pageRequest).map(this::convertOuter);

        Mono<Long> count = notificationRepository.countByReadAndEnabledTrue(read);

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<NotificationVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return notificationRepository.getByCodeAndEnabledTrue(code).doOnNext(notification ->
                notification.setRead(true)).flatMap(notificationRepository::save).map(this::convertOuter);
    }

    @Override
    public Mono<NotificationVO> create(NotificationDTO notificationDTO) {
        return accountRepository.getByUsernameAndEnabledTrue(notificationDTO.getReceiver()).flatMap(user ->
                        Mono.just(notificationDTO).map(dto -> {
                                    Notification notification = new Notification();
                                    BeanUtils.copyProperties(notificationDTO, notification);
                                    notification.setCode(this.generateCode());
                                    return notification;
                                })
                                .flatMap(notificationRepository::insert).map(this::convertOuter))
                .switchIfEmpty(Mono.error(new NoSuchElementException()));
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return notificationRepository.getByCodeAndEnabledTrue(code).flatMap(group ->
                notificationRepository.deleteById(group.getId()));
    }

    /**
     * 数据转换
     *
     * @param notification 信息
     * @return NotificationVO 输出对象
     */
    private NotificationVO convertOuter(Notification notification) {
        NotificationVO outer = new NotificationVO();
        BeanUtils.copyProperties(notification, outer);
        return outer;
    }

}
