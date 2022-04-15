package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.entity.AccessLog;
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import top.leafage.common.basic.AbstractBasicService;

/**
 * access log service impl.
 *
 * @author liwenqiang 2022/4/15 13:44
 **/
@Service
public class AccessLogServiceImpl extends AbstractBasicService implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public Page<AccessLogVO> retrieve(int page, int size) {
        return accessLogRepository.findAll(PageRequest.of(page, size)).map(this::convert);
    }

    @Override
    public AccessLogVO create(AccessLogDTO accessLogDTO) {
        AccessLog accessLog = new AccessLog();
        BeanUtils.copyProperties(accessLogDTO, accessLog);
        accessLog.setCode(this.generateCode());
        accessLogRepository.saveAndFlush(accessLog);
        return this.convert(accessLog);
    }

    private AccessLogVO convert(AccessLog accessLog) {
        AccessLogVO vo = new AccessLogVO();
        BeanUtils.copyProperties(accessLog, vo);
        return vo;
    }
}
