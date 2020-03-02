/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 开启日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(ArticleInfoServiceImpl.class);
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
        return this.fetchByBusinessIdId(businessId).map(this::convertOuter).flatMap(articleVO ->
                // 根据业务id获取相关内容
                contentInfoService.fetchByBusinessId(articleVO.getBusinessId()).map(contentInfo -> {
                    // 将内容设置到vo对像中
                    articleVO.setContent(contentInfo.getContent());
                    articleVO.setCatalog(contentInfo.getCatalog());
                    return articleVO;
                })
        );
    }

    @Override
    public Mono<ArticleVO> create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(articleDTO, info);
        info.setBusinessId(PrefixEnum.AT + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return articleInfoRepository.save(info).doOnSuccess(articleInfo -> {
            // 添加内容信息
            ContentInfo contentInfo = new ContentInfo();
            BeanUtils.copyProperties(articleDTO, contentInfo);
            contentInfo.setBusinessId(articleInfo.getBusinessId());
            contentInfoService.create(contentInfo);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String businessId, ArticleDTO articleDTO) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessIdId(businessId).flatMap(info -> {
            // 将信息复制到info
            BeanUtils.copyProperties(articleDTO, info);
            return articleInfoRepository.save(info).doOnSuccess(articleInfo ->
                    // 更新成功后，将内容信息更新
                    contentInfoService.fetchByBusinessId(articleInfo.getBusinessId()).doOnNext(contentInfo -> {
                        BeanUtils.copyProperties(articleDTO, contentInfo);
                        contentInfoService.modify(contentInfo.getBusinessId(), contentInfo);
                    })
            ).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Objects.requireNonNull(businessId);
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
