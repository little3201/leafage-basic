/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import io.leafage.common.basic.AbstractBasicService;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;

/**
 * posts service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private final PostsRepository postsRepository;
    private final PostsContentService postsContentService;

    public PostsServiceImpl(PostsRepository postsRepository, PostsContentService postsContentService) {
        this.postsRepository = postsRepository;
        this.postsContentService = postsContentService;
    }

    @Override
    public Flux<PostsVO> retrieve(int page, int size, String order) {
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtils.hasText(order) ? order : "modify_time");
        return postsRepository.findByEnabledTrue(PageRequest.of(page, size, sort)).map(this::convertOuter);
    }

    @Override
    public Mono<PostsContentVO> fetchContent(String code) {
        Asserts.notBlank(code, "code");
        return postsRepository.findByCodeAndEnabledTrue(code)
                .flatMap(posts -> {
                    posts.setViewed(posts.getViewed() + 1);
                    return postsRepository.save(posts);
                })
                .flatMap(posts -> {
                    // 将内容设置到vo对像中
                    PostsContentVO postsContentVO = new PostsContentVO();
                    BeanUtils.copyProperties(posts, postsContentVO);
                    // 根据业务id获取相关内容
                    return postsContentService.fetchByPostsId(posts.getId()).map(contentInfo -> {
                        postsContentVO.setContent(contentInfo.getContent());
                        postsContentVO.setCatalog(contentInfo.getCatalog());
                        return postsContentVO;
                    }).defaultIfEmpty(postsContentVO);
                });
    }

    @Override
    public Mono<PostsVO> create(PostsDTO postsDTO) {
        Posts info = new Posts();
        BeanUtils.copyProperties(postsDTO, info);
        info.setCode(this.generateCode());
        info.setEnabled(true);
        Mono<Posts> postsMono = postsRepository.insert(info)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .doOnSuccess(posts -> {
                    // 添加内容信息
                    PostsContent postsContent = new PostsContent();
                    BeanUtils.copyProperties(postsDTO, postsContent);
                    postsContent.setPostsId(posts.getId());
                    // 调用subscribe()方法，消费create订阅
                    postsContentService.create(postsContent).subscribe();
                });
        return postsMono.map(this::convertOuter);
    }

    @Override
    public Mono<PostsVO> modify(String code, PostsDTO postsDTO) {
        Asserts.notBlank(code, "code");
        Mono<Posts> postsMono = postsRepository.findByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(info -> {
                    // 将信息复制到info
                    BeanUtils.copyProperties(postsDTO, info);
                    return postsRepository.save(info)
                            .switchIfEmpty(Mono.error(RuntimeException::new))
                            .doOnSuccess(posts ->
                                    // 更新成功后，将内容信息更新
                                    postsContentService.fetchByPostsId(posts.getId()).subscribe(detailsInfo -> {
                                        BeanUtils.copyProperties(postsDTO, detailsInfo);
                                        // 调用subscribe()方法，消费modify订阅
                                        postsContentService.modify(posts.getId(), detailsInfo).subscribe();
                                    })
                            );
                });
        return postsMono.map(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return postsRepository.findByCodeAndEnabledTrue(code).flatMap(article ->
                postsRepository.deleteById(article.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private PostsVO convertOuter(Posts info) {
        PostsVO outer = new PostsVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

}
