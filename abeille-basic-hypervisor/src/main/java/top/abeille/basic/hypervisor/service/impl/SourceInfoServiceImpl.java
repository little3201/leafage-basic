/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.repository.SourceInfoRepository;
import top.abeille.basic.hypervisor.service.SourceInfoService;
import top.abeille.basic.hypervisor.vo.SourceVO;

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
    public Flux<SourceVO> retrieveAll(Sort sort) {
        return sourceInfoRepository.findAll(sort).map(article -> {
            SourceVO outer = new SourceVO();
            BeanUtils.copyProperties(article, outer);
            return outer;
        });
    }
}
