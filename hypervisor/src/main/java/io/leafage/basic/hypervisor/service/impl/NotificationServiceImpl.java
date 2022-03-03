package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.entity.Notification;
import io.leafage.basic.hypervisor.repository.NotificationRepository;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;

/**
 * notification service impl.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
@Service
public class NotificationServiceImpl extends ServletAbstractTreeNodeService<Notification> implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<NotificationVO> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByEnabledTrue(pageable).map(this::convertOuter);
    }

    @Override
    public NotificationVO fetch(String code) {
        Assert.notNull(code, "code must not null.");
        Notification notification = notificationRepository.getByCodeAndEnabledTrue(code);
        return this.convertOuter(notification);
    }

    @Override
    public NotificationVO create(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationDTO, notification);
        notification.setCode(this.generateCode());
        notificationRepository.save(notification);
        return this.convertOuter(notification);
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private NotificationVO convertOuter(Notification notification) {
        NotificationVO notificationVO = new NotificationVO();
        BeanUtils.copyProperties(notification, notificationVO);
        return notificationVO;
    }
}
