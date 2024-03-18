/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.AccessLog;
import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * access log service impl.
 *
 * @author wq li 2022/4/15 13:44
 **/
@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public Page<AccessLogVO> retrieve(int page, int size) {
        return accessLogRepository.findAll(PageRequest.of(page, size)).map(this::convert);
    }

    @Override
    public AccessLogVO create(AccessLogDTO dto) {
        AccessLog accessLog = new AccessLog();
        BeanCopier copier = BeanCopier.create(AccessLogDTO.class, AccessLog.class, false);
        copier.copy(dto, accessLog, null);

        accessLogRepository.saveAndFlush(accessLog);
        return this.convert(accessLog);
    }

    private AccessLogVO convert(AccessLog accessLog) {
        AccessLogVO vo = new AccessLogVO();
        BeanCopier copier = BeanCopier.create(AccessLog.class, AccessLogVO.class, false);
        copier.copy(accessLog, vo, null);
        return vo;
    }
}
