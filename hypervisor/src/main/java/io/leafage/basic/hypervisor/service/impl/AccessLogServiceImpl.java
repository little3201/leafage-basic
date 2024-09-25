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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * access log service impl.
 *
 * @author wq li
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    /**
     * <p>Constructor for AccessLogServiceImpl.</p>
     *
     * @param accessLogRepository a {@link io.leafage.basic.hypervisor.repository.AccessLogRepository} object
     */
    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AccessLogVO> retrieve(int page, int size, String sortBy, boolean descending) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return accessLogRepository.findAll(pageable).map(this::convert);
    }

    @Override
    public AccessLogVO fetch(Long id) {
        Assert.notNull(id, "access log id must not be null.");
        AccessLog accessLog = accessLogRepository.findById(id).orElse(null);
        if (accessLog == null) {
            return null;
        }
        return this.convert(accessLog);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccessLogVO create(AccessLogDTO dto) {
        AccessLog accessLog = new AccessLog();
        BeanCopier copier = BeanCopier.create(AccessLogDTO.class, AccessLog.class, false);
        copier.copy(dto, accessLog, null);

        accessLogRepository.saveAndFlush(accessLog);
        return this.convert(accessLog);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "access log id must not be null.");
        accessLogRepository.deleteById(id);
    }

    private AccessLogVO convert(AccessLog accessLog) {
        AccessLogVO vo = new AccessLogVO();
        BeanCopier copier = BeanCopier.create(AccessLog.class, AccessLogVO.class, false);
        copier.copy(accessLog, vo, null);
        return vo;
    }
}
