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
import top.abeille.basic.assets.document.ContentInfo;
import top.abeille.basic.assets.document.TranslationInfo;
import top.abeille.basic.assets.dto.TranslationDTO;
import top.abeille.basic.assets.repository.TranslationInfoRepository;
import top.abeille.basic.assets.service.ContentInfoService;
import top.abeille.basic.assets.service.TranslationInfoService;
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
public class TranslationInfoServiceImpl extends AbstractBasicService implements TranslationInfoService {

    private final TranslationInfoRepository translationInfoRepository;
    private final ContentInfoService contentInfoService;

    public TranslationInfoServiceImpl(TranslationInfoRepository translationInfoRepository, ContentInfoService contentInfoService) {
        this.translationInfoRepository = translationInfoRepository;
        this.contentInfoService = contentInfoService;
    }

    @Override
    public Flux<TranslationVO> retrieveAll(Sort sort) {
        return translationInfoRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<TranslationVO> fetchById(String businessId) {
        return fetchByTranslationId(businessId).map(this::convertOuter).flatMap(translationVO -> contentInfoService.fetchByBusinessIdId(businessId)
                .map(contentInfo -> {
                    translationVO.setContent(contentInfo.getContent());
                    translationVO.setCatalog(contentInfo.getCatalog());
                    return translationVO;
                }));
    }

    @Override
    public Mono<TranslationVO> create(TranslationDTO translationDTO) {
        TranslationInfo info = new TranslationInfo();
        BeanUtils.copyProperties(translationDTO, info);
        info.setBusinessId(PrefixEnum.TS + this.generateId());
        // 默认参数设置
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return translationInfoRepository.save(info).doOnSuccess(translationInfo -> {
            ContentInfo contentInfo = new ContentInfo();
            contentInfo.setBusinessId(translationInfo.getBusinessId());
            contentInfo.setContent(translationDTO.getContent());
            contentInfo.setCatalog(translationDTO.getCatalog());
            contentInfo.setModifier(translationInfo.getModifier());
            contentInfoService.create(contentInfo);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<TranslationVO> modify(String businessId, TranslationDTO translationDTO) {
        return this.fetchByTranslationId(businessId).flatMap(info -> {
            BeanUtils.copyProperties(translationDTO, info);
            return translationInfoRepository.save(info).doOnSuccess(translationInfo -> contentInfoService.fetchByBusinessIdId(businessId).flatMap(contentInfo -> {
                contentInfo.setContent(translationDTO.getContent());
                contentInfo.setCatalog(translationDTO.getCatalog());
                contentInfo.setModifier(translationInfo.getModifier());
                return contentInfoService.modify(businessId, contentInfo);
            })).map(this::convertOuter);
        });
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<TranslationInfo> fetchByTranslationId(String businessId) {
        Objects.requireNonNull(businessId);
        TranslationInfo info = new TranslationInfo();
        info.setBusinessId(businessId);
        return translationInfoRepository.findOne(Example.of(info));
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
