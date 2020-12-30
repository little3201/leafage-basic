/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.PostsContent;
import top.abeille.basic.assets.repository.PostsContentRepository;
import top.abeille.basic.assets.service.PostsContentService;

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
    public Mono<PostsContent> modify(String postsId, PostsContent postsContentInfo) {
        Asserts.notBlank(postsId, "postsId");
        return this.fetchByPostsId(postsId).flatMap(details -> {
            BeanUtils.copyProperties(postsContentInfo, details);
            return postsContentRepository.save(details);
        });
    }

    @Override
    public Mono<PostsContent> fetchByPostsId(String postsId) {
        return postsContentRepository.findByPostsIdAndEnabledTrue(postsId);
    }

}
