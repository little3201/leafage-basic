/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Example;
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
        detailsInfo.setEnabled(Boolean.TRUE);
        detailsInfo.setModifyTime(LocalDateTime.now());
        return detailsRepository.insert(detailsInfo);
    }

    @Override
    public Mono<DetailsInfo> modify(String businessId, DetailsInfo detailsInfo) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchByBusinessId(businessId).flatMap(details -> {
            BeanCopier.create(DetailsInfo.class, DetailsInfo.class, false).copy(detailsInfo, details, null);
            details.setModifyTime(LocalDateTime.now());
            return detailsRepository.save(details);
        });
    }

    @Override
    public Mono<DetailsInfo> fetchByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        DetailsInfo info = new DetailsInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return detailsRepository.findOne(Example.of(info));
    }
}
