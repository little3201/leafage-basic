package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import reactor.core.publisher.Flux;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * notification service
 *
 * @author liwenqiang 2022-02-10 13:49
 */
public interface NotificationService extends ReactiveBasicService<NotificationDTO, NotificationVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param read 是否已读
     * @return 数据集
     */
    Flux<NotificationVO> retrieve(int page, int size, boolean read);
}
