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
import top.abeille.basic.assets.document.ArticleInfo;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Objects;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class ArticleInfoServiceImpl extends AbstractBasicService implements ArticleInfoService {

    private final ArticleInfoRepository articleInfoRepository;

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository) {
        this.articleInfoRepository = articleInfoRepository;
    }

    @Override
    public Flux<ArticleVO> retrieveAll(Sort sort) {
        return articleInfoRepository.findAll(sort).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> fetchById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessIdId(businessId).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(articleDTO, info);
        info.setBusinessId(this.getDateValue());
        info.setEnabled(Boolean.TRUE);
        return articleInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String businessId, ArticleDTO articleDTO) {
        return this.fetchByBusinessIdId(businessId).flatMap(articleInfo -> {
            BeanUtils.copyProperties(articleDTO, articleInfo);
            return articleInfoRepository.save(articleInfo).filter(Objects::nonNull).map(this::convertOuter);
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
