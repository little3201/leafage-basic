/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.api.HypervisorApi;
import top.abeille.basic.assets.api.bo.UserBO;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.ArticleInfo;
import top.abeille.basic.assets.document.DetailsInfo;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.repository.ArticleRepository;
import top.abeille.basic.assets.service.ArticleService;
import top.abeille.basic.assets.service.DetailsService;
import top.abeille.basic.assets.vo.ArticleDetailsVO;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class ArticleServiceImpl extends AbstractBasicService implements ArticleService {

    private final ArticleRepository articleRepository;
    private final DetailsService detailsService;
    private final HypervisorApi hypervisorApi;

    public ArticleServiceImpl(ArticleRepository articleRepository, DetailsService detailsService,
                              HypervisorApi hypervisorApi) {
        this.articleRepository = articleRepository;
        this.detailsService = detailsService;
        this.hypervisorApi = hypervisorApi;
    }

    @Override
    public Flux<ArticleVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return articleRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleDetailsVO> fetchDetailsByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchByBusinessId(businessId).flatMap(articleVO -> {
                    // 将内容设置到vo对像中
                    ArticleDetailsVO detailsVO = new ArticleDetailsVO();
                    detailsVO.setBusinessId(articleVO.getBusinessId());
                    detailsVO.setTitle(articleVO.getTitle());
                    detailsVO.setSubtitle(articleVO.getSubtitle());
                    detailsVO.setImageUrl(articleVO.getImageUrl());
                    // 根据业务id获取相关内容
                    return detailsService.fetchByBusinessId(articleVO.getBusinessId()).map(contentInfo -> {
                        detailsVO.setContent(contentInfo.getContent());
                        detailsVO.setCatalog(contentInfo.getCatalog());
                        return detailsVO;
                    }).defaultIfEmpty(detailsVO);
                }
        );
    }

    @Override
    public Mono<ArticleVO> create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        info.setTitle(articleDTO.getTitle());
        info.setSubtitle(articleDTO.getSubtitle());
        info.setImageUrl(articleDTO.getImageUrl());
        info.setBusinessId(PrefixEnum.AT + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifier(articleDTO.getModifier());
        info.setModifyTime(LocalDateTime.now());
        return articleRepository.insert(info).doOnSuccess(articleInfo -> {
            // 添加内容信息
            DetailsInfo detailsInfo = new DetailsInfo();
            detailsInfo.setBusinessId(articleInfo.getBusinessId());
            detailsInfo.setContent(articleDTO.getContent());
            detailsInfo.setCatalog(articleDTO.getCatalog());
            detailsInfo.setModifier(articleInfo.getModifier());
            detailsInfo.setEnabled(Boolean.TRUE);
            detailsInfo.setModifyTime(articleInfo.getModifyTime());
            // 调用subscribe()方法，消费create订阅
            detailsService.create(detailsInfo).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String businessId, ArticleDTO articleDTO) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).flatMap(info -> {
            info.setTitle(articleDTO.getTitle());
            info.setSubtitle(articleDTO.getSubtitle());
            info.setImageUrl(articleDTO.getImageUrl());
            info.setModifier(articleDTO.getModifier());
            info.setModifyTime(LocalDateTime.now());
            return articleRepository.save(info).doOnSuccess(articleInfo ->
                    // 更新成功后，将内容信息更新
                    detailsService.fetchByBusinessId(articleInfo.getBusinessId()).doOnNext(detailsInfo -> {
                        detailsInfo.setBusinessId(articleInfo.getBusinessId());
                        detailsInfo.setContent(articleDTO.getContent());
                        detailsInfo.setCatalog(articleDTO.getCatalog());
                        detailsInfo.setModifier(articleInfo.getModifier());
                        detailsInfo.setEnabled(Boolean.TRUE);
                        detailsInfo.setModifyTime(articleInfo.getModifyTime());
                        // 调用subscribe()方法，消费modify订阅
                        detailsService.modify(detailsInfo.getBusinessId(), detailsInfo).subscribe();
                    }).subscribe()
            ).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).flatMap(article -> articleRepository.deleteById(article.getId()));
    }

    @Override
    public Mono<ArticleVO> fetchByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<ArticleInfo> fetchInfo(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        ArticleInfo info = new ArticleInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return articleRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private ArticleVO convertOuter(ArticleInfo info) {
        ArticleVO outer = new ArticleVO();
        outer.setBusinessId(info.getBusinessId());
        outer.setTitle(info.getTitle());
        outer.setSubtitle(info.getSubtitle());
        outer.setImageUrl(info.getImageUrl());
        UserBO userBO = hypervisorApi.fetchUserByBusinessId(info.getModifier()).block();
        outer.setAuthor(userBO);
        return outer;
    }

}
