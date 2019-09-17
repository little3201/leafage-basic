/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.entity.SourceInfo;
import top.abeille.basic.hypervisor.repository.SourceInfoRepository;
import top.abeille.basic.hypervisor.service.SourceInfoService;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class SourceInfoServiceImpl implements SourceInfoService {

    private final SourceInfoRepository sourceInfoRepository;

    public SourceInfoServiceImpl(SourceInfoRepository sourceInfoRepository) {
        this.sourceInfoRepository = sourceInfoRepository;
    }

    @Override
    public Flux<SourceInfo> findAll() {
        return sourceInfoRepository.findAll();
    }
}
