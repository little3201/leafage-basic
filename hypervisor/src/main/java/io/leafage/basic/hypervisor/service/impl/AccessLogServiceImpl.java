package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.AccessLog;
import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

/**
 * record service impl
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class AccessLogServiceImpl extends AbstractBasicService implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public Mono<Page<AccessLogVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<AccessLogVO> voFlux = accessLogRepository.findByEnabledTrue(pageRequest).map(this::convert);

        Mono<Long> count = accessLogRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<AccessLogVO> create(AccessLogDTO accessLogDTO) {
        AccessLog info = new AccessLog();
        BeanUtils.copyProperties(accessLogDTO, info);
        info.setCode(this.generateCode());
        return accessLogRepository.insert(info).map(this::convert);
    }

    /**
     * 对象转换
     * @param info 数据对象
     * @return 输出对象
     */
    private AccessLogVO convert(AccessLog info) {
        AccessLogVO outer = new AccessLogVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
