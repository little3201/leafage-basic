/*
 * Copyright (c) 2024.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.OperationLog;
import io.leafage.basic.hypervisor.dto.OperationLogDTO;
import io.leafage.basic.hypervisor.repository.OperationLogRepository;
import io.leafage.basic.hypervisor.service.OperationLogService;
import io.leafage.basic.hypervisor.vo.OperationLogVO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * operation log service impl.
 *
 * @author wq li
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository operationLogRepository;

    /**
     * <p>Constructor for AccessLogServiceImpl.</p>
     *
     * @param operationLogRepository a {@link OperationLogRepository} object
     */
    public OperationLogServiceImpl(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OperationLogVO> retrieve(int page, int size, String sortBy, boolean descending, String operation) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<OperationLog> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(operation)) {
                predicates.add(cb.like(root.get("operation"), "%" + operation + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return operationLogRepository.findAll(spec, pageable).map(this::convert);
    }

    @Override
    public OperationLogVO fetch(Long id) {
        Assert.notNull(id, "id must not be null.");

        return operationLogRepository.findById(id).map(this::convert).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationLogVO create(OperationLogDTO dto) {
        OperationLog operationLog = new OperationLog();
        BeanCopier copier = BeanCopier.create(OperationLogDTO.class, OperationLog.class, false);
        copier.copy(dto, operationLog, null);

        operationLogRepository.saveAndFlush(operationLog);
        return this.convert(operationLog);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "id must not be null.");
        operationLogRepository.deleteById(id);
    }

    private OperationLogVO convert(OperationLog operationLog) {
        OperationLogVO vo = new OperationLogVO();
        BeanCopier copier = BeanCopier.create(OperationLog.class, OperationLogVO.class, false);
        copier.copy(operationLog, vo, null);
        return vo;
    }
}
