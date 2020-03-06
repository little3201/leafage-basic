/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.SourceInfo;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.repository.SourceRepository;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Objects;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class SourceServiceImpl extends AbstractBasicService implements SourceService {

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Flux<SourceVO> retrieveAll(Sort sort) {
        return sourceRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> create(SourceDTO sourceDTO) {
        SourceInfo info = new SourceInfo();
        BeanUtils.copyProperties(sourceDTO, info);
        info.setBusinessId(this.generateId());
        info.setEnabled(Boolean.TRUE);
        return sourceRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> modify(String businessId, SourceDTO sourceDTO) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(info -> {
            BeanUtils.copyProperties(sourceDTO, info);
            return sourceRepository.save(info);
        }).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<SourceInfo> fetchInfo(String businessId) {
        Objects.requireNonNull(businessId);
        SourceInfo info = new SourceInfo();
        info.setBusinessId(businessId);
        return sourceRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private SourceVO convertOuter(SourceInfo info) {
        SourceVO outer = new SourceVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
