/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.DetailsInfo;
import top.abeille.basic.assets.repository.DetailsRepository;
import top.abeille.basic.assets.service.DetailsService;

import java.time.LocalDateTime;

@Service
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;

    public DetailsServiceImpl(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    @Override
    public Mono<DetailsInfo> create(DetailsInfo detailsInfo) {
        detailsInfo.setEnabled(true);
        detailsInfo.setModifyTime(LocalDateTime.now());
        return detailsRepository.insert(detailsInfo);
    }

    @Override
    public Mono<DetailsInfo> modify(String articleId, DetailsInfo detailsInfo) {
        Asserts.notBlank(articleId, "articleId");
        return this.fetchByArticleId(articleId).flatMap(details -> {
            BeanUtils.copyProperties(detailsInfo, details);
            details.setModifyTime(LocalDateTime.now());
            return detailsRepository.save(details);
        });
    }

    @Override
    public Mono<DetailsInfo> fetchByArticleId(String articleId) {
        return detailsRepository.findByArticleIdAndEnabledTrue(articleId);
    }

}
