/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.repository.PostsContentRepository;
import io.leafage.basic.assets.service.PostsContentService;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * posts content service impl
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
    public Mono<PostsContent> modify(ObjectId postsId, PostsContent postsContent) {
        Assert.notNull(postsId, "postsId is null");
        return this.fetchByPostsId(postsId).doOnNext(details -> BeanUtils.copyProperties(postsContent, details))
                .flatMap(postsContentRepository::save);
    }

    @Override
    public Mono<PostsContent> fetchByPostsId(ObjectId postsId) {
        Assert.notNull(postsId, "postsId is null");
        return postsContentRepository.getByPostsIdAndEnabledTrue(postsId);
    }

}
