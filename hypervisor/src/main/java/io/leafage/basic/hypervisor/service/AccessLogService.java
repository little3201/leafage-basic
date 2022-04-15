package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * access log service.
 *
 * @author wq 2022/4/15 13:43
 **/
public interface AccessLogService extends ServletBasicService<AccessLogDTO, AccessLogVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<AccessLogVO> retrieve(int page, int size);
}
