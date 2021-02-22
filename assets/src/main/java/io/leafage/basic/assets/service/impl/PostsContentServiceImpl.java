/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.repository.PostsContentRepository;
import io.leafage.basic.assets.service.PostsContentService;
import org.apache.http.util.Asserts;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 内容service接口实现
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Service
public class PostsContentServiceImpl implements PostsContentService {

    private final PostsContentRepository postsContentRepository;

    public PostsContentServiceImpl(PostsContentRepository postsContentRepository) {
        this.postsContentRepository = postsContentRepository;
    }

    @Override
    public Mono<PostsContent> create(PostsContent postsContent) {
        return postsContentRepository.insert(postsContent);
    }

    @Override
    public Mono<PostsContent> modify(ObjectId postsId, PostsContent postsContentInfo) {
        Asserts.notBlank(postsId.toHexString(), "postsId");
        return this.fetchByPostsId(postsId).flatMap(details -> {
            BeanUtils.copyProperties(postsContentInfo, details);
            return postsContentRepository.save(details);
        });
    }

    @Override
    public Mono<PostsContent> fetchByPostsId(ObjectId postsId) {
        return postsContentRepository.getByPostsIdAndEnabledTrue(postsId);
    }

}
