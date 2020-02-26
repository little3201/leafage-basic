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
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.ArticleInfo;
import top.abeille.basic.assets.document.ContentInfo;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.service.ContentInfoService;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class ArticleInfoServiceImpl extends AbstractBasicService implements ArticleInfoService {

    private final ArticleInfoRepository articleInfoRepository;
    private final ContentInfoService contentInfoService;

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository, ContentInfoService contentInfoService) {
        this.articleInfoRepository = articleInfoRepository;
        this.contentInfoService = contentInfoService;
    }

    @Override
    public Flux<ArticleVO> retrieveAll(Sort sort) {
        return articleInfoRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> fetchById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessIdId(businessId).map(this::convertOuter).flatMap(articleVO -> contentInfoService.fetchByBusinessIdId(businessId)
                .map(contentInfo -> {
                    articleVO.setContent(contentInfo.getContent());
                    articleVO.setCatalog(contentInfo.getCatalog());
                    return articleVO;
                }));
    }

    @Override
    public Mono<ArticleVO> create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(articleDTO, info);
        info.setBusinessId(PrefixEnum.AT + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return articleInfoRepository.save(info).doOnSuccess(articleInfo -> {
            ContentInfo contentInfo = new ContentInfo();
            contentInfo.setBusinessId(articleInfo.getBusinessId());
            contentInfo.setContent(articleDTO.getContent());
            contentInfo.setCatalog(articleDTO.getCatalog());
            contentInfo.setModifier(articleInfo.getModifier());
            contentInfoService.create(contentInfo);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String businessId, ArticleDTO articleDTO) {
        return this.fetchByBusinessIdId(businessId).flatMap(articleInfo -> {
            BeanUtils.copyProperties(articleDTO, articleInfo);
            return articleInfoRepository.save(articleInfo).doOnSuccess(info -> contentInfoService.fetchByBusinessIdId(businessId).flatMap(contentInfo -> {
                contentInfo.setContent(articleDTO.getContent());
                contentInfo.setCatalog(articleDTO.getCatalog());
                contentInfo.setModifier(info.getModifier());
                return contentInfoService.modify(businessId, contentInfo);
            })).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        return this.fetchByBusinessIdId(businessId).flatMap(article -> articleInfoRepository.deleteById(article.getId()));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<ArticleInfo> fetchByBusinessIdId(String businessId) {
        Objects.requireNonNull(businessId);
        ArticleInfo info = new ArticleInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return articleInfoRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private ArticleVO convertOuter(ArticleInfo info) {
        ArticleVO outer = new ArticleVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
