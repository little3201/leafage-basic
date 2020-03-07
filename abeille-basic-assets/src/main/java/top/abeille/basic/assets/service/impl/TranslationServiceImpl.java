/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.DetailsInfo;
import top.abeille.basic.assets.document.TranslationInfo;
import top.abeille.basic.assets.dto.TranslationDTO;
import top.abeille.basic.assets.repository.TranslationRepository;
import top.abeille.basic.assets.service.DetailsService;
import top.abeille.basic.assets.service.TranslationService;
import top.abeille.basic.assets.vo.TranslationDetailsVO;
import top.abeille.basic.assets.vo.TranslationVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 翻译信息service实现
 *
 * @author liwenqiang 2020/2/13 20:24
 **/
@Service
public class TranslationServiceImpl extends AbstractBasicService implements TranslationService {

    private final TranslationRepository translationRepository;
    private final DetailsService detailsService;

    public TranslationServiceImpl(TranslationRepository translationRepository, DetailsService detailsService) {
        this.translationRepository = translationRepository;
        this.detailsService = detailsService;
    }

    @Override
    public Flux<TranslationVO> retrieveAll(Sort sort) {
        return translationRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<TranslationDetailsVO> fetchDetailsByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessId(businessId).flatMap(translationVO -> {
                    // 将内容设置到vo对像中
                    TranslationDetailsVO detailsVO = new TranslationDetailsVO();
                    BeanUtils.copyProperties(translationVO, detailsVO);
                    // 根据业务id获取相关内容
                    return detailsService.fetchByBusinessId(businessId).map(contentInfo -> {
                        detailsVO.setContent(contentInfo.getContent());
                        detailsVO.setCatalog(contentInfo.getCatalog());
                        return detailsVO;
                    }).defaultIfEmpty(detailsVO);
                }
        );
    }

    @Override
    public Mono<TranslationVO> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<TranslationVO> create(TranslationDTO translationDTO) {
        TranslationInfo info = new TranslationInfo();
        BeanUtils.copyProperties(translationDTO, info);
        info.setBusinessId(PrefixEnum.TS + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return translationRepository.save(info).doOnSuccess(translationInfo -> {
            // 添加内容信息
            DetailsInfo detailsInfo = new DetailsInfo();
            BeanUtils.copyProperties(translationDTO, detailsInfo);
            detailsInfo.setBusinessId(translationInfo.getBusinessId());
            // 这里需要调用subscribe()方法来订阅执行，否则数据不会入库
            detailsService.create(detailsInfo).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<TranslationVO> modify(String businessId, TranslationDTO translationDTO) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(info -> {
            // 将信息复制到info
            BeanUtils.copyProperties(translationDTO, info);
            return translationRepository.save(info).doOnSuccess(translationInfo ->
                    // 更新成功后，将内容信息更新
                    detailsService.fetchByBusinessId(businessId).doOnNext(contentInfo -> {
                        BeanUtils.copyProperties(translationDTO, contentInfo);
                        // 这里需要调用subscribe()方法，否则数据不会入库
                        detailsService.modify(contentInfo.getBusinessId(), contentInfo).subscribe();
                    }).subscribe()
            ).map(this::convertOuter);
        });
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<TranslationInfo> fetchInfo(String businessId) {
        Objects.requireNonNull(businessId);
        TranslationInfo info = new TranslationInfo();
        info.setBusinessId(businessId);
        return translationRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private TranslationVO convertOuter(TranslationInfo info) {
        TranslationVO outer = new TranslationVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

}
