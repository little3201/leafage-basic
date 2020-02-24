/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.SourceInfo;
import top.abeille.basic.assets.dto.SourceDTO;
import top.abeille.basic.assets.repository.SourceInfoRepository;
import top.abeille.basic.assets.service.SourceInfoService;
import top.abeille.basic.assets.vo.SourceVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Objects;

/**
 * 资源信息service实现
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
@Service
public class SourceInfoServiceImpl extends AbstractBasicService implements SourceInfoService {

    private final SourceInfoRepository sourceInfoRepository;

    public SourceInfoServiceImpl(SourceInfoRepository sourceInfoRepository) {
        this.sourceInfoRepository = sourceInfoRepository;
    }

    @Override
    public Flux<SourceVO> retrieveAll(Sort sort) {
        return sourceInfoRepository.findAll(sort).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> fetchById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessIdId(businessId).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> create(SourceDTO sourceDTO) {
        SourceInfo info = new SourceInfo();
        BeanUtils.copyProperties(sourceDTO, info);
        info.setBusinessId(this.getDateValue());
        info.setEnabled(Boolean.TRUE);
        return sourceInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> modify(String businessId, SourceDTO sourceDTO) {
        return this.fetchByBusinessIdId(businessId).flatMap(articleInfo -> {
            BeanUtils.copyProperties(sourceDTO, articleInfo);
            return sourceInfoRepository.save(articleInfo).filter(Objects::nonNull).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        return this.fetchByBusinessIdId(businessId).flatMap(article -> sourceInfoRepository.deleteById(article.getId()));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<SourceInfo> fetchByBusinessIdId(String businessId) {
        Objects.requireNonNull(businessId);
        SourceInfo info = new SourceInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return sourceInfoRepository.findOne(Example.of(info));
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
