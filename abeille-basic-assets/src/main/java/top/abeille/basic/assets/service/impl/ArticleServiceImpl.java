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
import top.abeille.basic.assets.api.HypervisorApi;
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
import java.util.Objects;

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
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessId(businessId).flatMap(articleVO -> {
                    // 将内容设置到vo对像中
                    ArticleDetailsVO detailsVO = new ArticleDetailsVO();
                    BeanUtils.copyProperties(articleVO, detailsVO);
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
        BeanUtils.copyProperties(articleDTO, info);
        info.setBusinessId(PrefixEnum.AT + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return articleRepository.insert(info).doOnSuccess(articleInfo -> {
            // 添加内容信息
            DetailsInfo detailsInfo = new DetailsInfo();
            BeanUtils.copyProperties(articleDTO, detailsInfo);
            detailsInfo.setBusinessId(articleInfo.getBusinessId());
            // 这里需要调用subscribe()方法，否则数据不会入库
            detailsService.create(detailsInfo).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String businessId, ArticleDTO articleDTO) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(info -> {
            // 将信息复制到info
            BeanUtils.copyProperties(articleDTO, info);
            return articleRepository.save(info).doOnSuccess(articleInfo ->
                    // 更新成功后，将内容信息更新
                    detailsService.fetchByBusinessId(articleInfo.getBusinessId()).doOnNext(detailsInfo -> {
                        BeanUtils.copyProperties(articleDTO, detailsInfo);
                        // 这里需要调用subscribe()方法，否则数据不会入库
                        detailsService.modify(detailsInfo.getBusinessId(), detailsInfo).subscribe();
                    }).subscribe()
            ).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(article -> articleRepository.deleteById(article.getId()));
    }

    @Override
    public Mono<ArticleVO> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<ArticleInfo> fetchInfo(String businessId) {
        Objects.requireNonNull(businessId);
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
        BeanUtils.copyProperties(info, outer);
        hypervisorApi.fetchUserByBusinessId(info.getModifier())
                .switchIfEmpty(Mono.error(() -> new RuntimeException("调用服务发生异常")))
                .subscribe(outer::setAuthor);
        return outer;
    }

}
