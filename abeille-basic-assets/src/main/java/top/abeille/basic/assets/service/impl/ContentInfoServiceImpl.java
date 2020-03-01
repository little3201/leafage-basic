/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.ContentInfo;
import top.abeille.basic.assets.repository.ContentInfoRepository;
import top.abeille.basic.assets.service.ContentInfoService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ContentInfoServiceImpl implements ContentInfoService {

    private final ContentInfoRepository contentInfoRepository;

    public ContentInfoServiceImpl(ContentInfoRepository contentInfoRepository) {
        this.contentInfoRepository = contentInfoRepository;
    }

    @Override
    public Mono<ContentInfo> create(ContentInfo contentInfo) {
        contentInfo.setEnabled(Boolean.TRUE);
        contentInfo.setModifyTime(LocalDateTime.now());
        return contentInfoRepository.save(contentInfo);
    }

    @Override
    public Mono<ContentInfo> modify(String businessId, ContentInfo contentInfo) {
        return this.fetchByBusinessId(businessId).flatMap(content -> {
            BeanUtils.copyProperties(contentInfo, content);
            content.setModifyTime(LocalDateTime.now());
            return contentInfoRepository.save(content);
        });
    }

    @Override
    public Mono<ContentInfo> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        ContentInfo info = new ContentInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return contentInfoRepository.findOne(Example.of(info));
    }
}
