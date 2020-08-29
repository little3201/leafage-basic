/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
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
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.basic.assets.vo.DetailsVO;
import top.abeille.basic.assets.vo.StatisticsVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        return articleRepository.findAll(sort).flatMap(this::convertOuter);
    }

    @Override
    public Mono<DetailsVO> fetchDetailsByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchByBusinessId(businessId).flatMap(articleVO -> {
                    // 将内容设置到vo对像中
                    DetailsVO detailsVO = new DetailsVO();
                    BeanUtils.copyProperties(articleVO, detailsVO);
                    // 根据业务id获取相关内容
                    return detailsService.fetchByBusinessId(articleVO.getBusinessId()).map(contentInfo -> {
                        detailsVO.setOriginal(contentInfo.getOriginal());
                        detailsVO.setContent(contentInfo.getContent());
                        detailsVO.setCatalog(contentInfo.getCatalog());
                        return detailsVO;
                    }).defaultIfEmpty(detailsVO);
                }
        );
    }

    @Override
    public Flux<StatisticsVO> statistics() {
        LocalDate now = LocalDate.now();
        int monthSize = this.lastDayOfMonth(now).getDayOfMonth();
        LocalDate firstDay = this.firstDayOfMonth(now);
        List<StatisticsVO> voList = new ArrayList<>(monthSize);
        AtomicInteger ai = new AtomicInteger(0);
        return Flux.fromIterable(voList).map(statisticsVO -> {
            statisticsVO = new StatisticsVO();
            int day = ai.incrementAndGet();
            statisticsVO.setLabel(day);
            articleRepository.countByModifyTimeBetween(firstDay.plusDays(day), firstDay.plusDays(day + 1))
                    .subscribe(statisticsVO::setValue);
            return statisticsVO;
        });
    }

    @Override
    public Flux<ArticleVO> fetchTop10() {
        return null;
    }

    @Override
    public Mono<ArticleVO> create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(articleDTO, info);
        info.setBusinessId(PrefixEnum.AT + this.generateId());
        info.setEnabled(true);
        info.setModifyTime(LocalDateTime.now());
        return articleRepository.insert(info).doOnSuccess(articleInfo -> {
            // 添加内容信息
            DetailsInfo detailsInfo = new DetailsInfo();
            BeanUtils.copyProperties(articleDTO, detailsInfo);
            detailsInfo.setBusinessId(articleInfo.getBusinessId());
            // 调用subscribe()方法，消费create订阅
            detailsService.create(detailsInfo).subscribe();
        }).flatMap(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String businessId, ArticleDTO articleDTO) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).flatMap(info -> {
            // 将信息复制到info
            BeanUtils.copyProperties(articleDTO, info);
            info.setModifyTime(LocalDateTime.now());
            return articleRepository.save(info).doOnSuccess(articleInfo ->
                    // 更新成功后，将内容信息更新
                    detailsService.fetchByBusinessId(articleInfo.getBusinessId()).doOnNext(detailsInfo -> {
                        BeanUtils.copyProperties(articleDTO, detailsInfo);
                        // 调用subscribe()方法，消费modify订阅
                        detailsService.modify(detailsInfo.getBusinessId(), detailsInfo).subscribe();
                    }).subscribe()
            ).flatMap(this::convertOuter);
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
        return this.fetchInfo(businessId).flatMap(this::convertOuter);
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
        info.setEnabled(true);
        return articleRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private Mono<ArticleVO> convertOuter(ArticleInfo info) {
        return Mono.just(info).map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            return articleVO;
        }).doOnNext(outer -> hypervisorApi.fetchUserByBusinessId(info.getModifier())
                .subscribe(outer::setAuthor));
    }

}
