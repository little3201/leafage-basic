package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * record service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccessLogService extends ReactiveBasicService<AccessLogDTO, AccessLogVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<AccessLogVO>> retrieve(int page, int size);
}
