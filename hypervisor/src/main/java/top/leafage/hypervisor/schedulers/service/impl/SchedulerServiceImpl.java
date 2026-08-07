/*
 * Copyright(c) 2019-present the original author or authors.
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

package top.leafage.hypervisor.schedulers.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import top.leafage.hypervisor.schedulers.domain.Scheduler;
import top.leafage.hypervisor.schedulers.domain.dto.SchedulerDTO;
import top.leafage.hypervisor.schedulers.domain.vo.SchedulerVO;
import top.leafage.hypervisor.schedulers.repository.SchedulerRepository;
import top.leafage.hypervisor.schedulers.service.SchedulerService;

import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;

/**
 * service for scheduler_logs.
 *
 * @author wq li
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

    private static final BeanCopier copier = BeanCopier.create(SchedulerDTO.class, Scheduler.class, false);
    private final SchedulerRepository schedulerRepository;

    /**
     * Constructor for SchedulerServiceImpl.
     *
     * @param schedulerRepository a {@link SchedulerRepository} object
     */
    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Override
    public Page<SchedulerVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Scheduler> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

        return schedulerRepository.findAll(spec, pageable)
                .map(SchedulerVO::from);
    }

    @Override
    public SchedulerVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return schedulerRepository.findById(id)
                .map(SchedulerVO::from)
                .orElseThrow(() -> new EntityNotFoundException("scheduler log not found: " + id));
    }

    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!schedulerRepository.existsById(id)) {
            throw new EntityNotFoundException("scheduler not found: " + id);
        }
        return schedulerRepository.enableById(id) > 0;
    }

    @Override
    public boolean disable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!schedulerRepository.existsById(id)) {
            throw new EntityNotFoundException("scheduler not found: " + id);
        }
        return schedulerRepository.enableById(id) > 0;
    }

    @Override
    public SchedulerVO create(SchedulerDTO dto) {
        if (schedulerRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Scheduler entity = schedulerRepository.save(SchedulerDTO.toEntity(dto));
        return SchedulerVO.from(entity);
    }

    @Override
    public SchedulerVO modify(Long id, SchedulerDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Scheduler existing = schedulerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dictionary not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                schedulerRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }

        copier.copy(dto, existing, null);
        Scheduler entity = schedulerRepository.save(existing);
        return SchedulerVO.from(entity);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!schedulerRepository.existsById(id)) {
            throw new EntityNotFoundException("scheduler log not found: " + id);
        }
        schedulerRepository.deleteById(id);
    }

}
