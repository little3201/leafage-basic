package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * notification service.
 *
 * @author liwenqiang 2022/1/29 17:34
 **/
public interface NotificationService extends ServletBasicService<NotificationDTO, NotificationVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<NotificationVO> retrieve(int page, int size);
}
