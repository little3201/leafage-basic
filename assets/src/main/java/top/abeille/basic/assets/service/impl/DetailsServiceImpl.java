/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Details;
import top.abeille.basic.assets.repository.DetailsRepository;
import top.abeille.basic.assets.service.DetailsService;

/**
 * 内容service接口实现
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Service
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;

    public DetailsServiceImpl(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    @Override
    public Mono<Details> create(Details details) {
        return detailsRepository.insert(details);
    }

    @Override
    public Mono<Details> modify(String articleId, Details detailsInfo) {
        Asserts.notBlank(articleId, "articleId");
        return this.fetchByArticleId(articleId).flatMap(details -> {
            BeanUtils.copyProperties(detailsInfo, details);
            return detailsRepository.save(details);
        });
    }

    @Override
    public Mono<Details> fetchByArticleId(String articleId) {
        return detailsRepository.findByArticleIdAndEnabledTrue(articleId);
    }

}
