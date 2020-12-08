/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Details;
import top.abeille.basic.assets.document.Posts;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.DetailsService;
import top.abeille.basic.assets.service.PostsService;
import top.abeille.basic.assets.vo.DetailsVO;
import top.abeille.basic.assets.vo.PostsVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private final PostsRepository postsRepository;
    private final DetailsService detailsService;

    public PostsServiceImpl(PostsRepository postsRepository, DetailsService detailsService) {
        this.postsRepository = postsRepository;
        this.detailsService = detailsService;
    }

    @Override
    public Flux<PostsVO> retrieveAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return postsRepository.findByEnabledTrue(PageRequest.of(page, size, sort)).map(this::convertOuter);
    }

    @Override
    public Mono<DetailsVO> fetchDetailsByCode(String code) {
        Asserts.notBlank(code, "code");
        return postsRepository.findByCodeAndEnabledTrue(code).flatMap(posts -> {
                    // 将内容设置到vo对像中
                    DetailsVO detailsVO = new DetailsVO();
                    BeanUtils.copyProperties(posts, detailsVO);
                    // 根据业务id获取相关内容
                    return detailsService.fetchByArticleId(posts.getId()).map(contentInfo -> {
                        detailsVO.setOriginal(contentInfo.getOriginal());
                        detailsVO.setContent(contentInfo.getContent());
                        detailsVO.setCatalog(contentInfo.getCatalog());
                        return detailsVO;
                    }).defaultIfEmpty(detailsVO);
                }
        );
    }

    @Override
    public Mono<PostsVO> create(PostsDTO postsDTO) {
        Posts info = new Posts();
        BeanUtils.copyProperties(postsDTO, info);
        info.setCode(this.generateCode());
        info.setEnabled(true);
        info.setModifyTime(LocalDateTime.now());
        return postsRepository.insert(info).doOnSuccess(posts -> {
            // 添加内容信息
            Details details = new Details();
            BeanUtils.copyProperties(postsDTO, details);
            details.setArticleId(posts.getId());
            // 调用subscribe()方法，消费create订阅
            detailsService.create(details).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<PostsVO> modify(String code, PostsDTO postsDTO) {
        Asserts.notBlank(code, "code");
        return postsRepository.findByCodeAndEnabledTrue(code).flatMap(info -> {
            // 将信息复制到info
            BeanUtils.copyProperties(postsDTO, info);
            info.setModifyTime(LocalDateTime.now());
            return postsRepository.save(info).doOnSuccess(posts ->
                    // 更新成功后，将内容信息更新
                    detailsService.fetchByArticleId(posts.getId()).doOnNext(detailsInfo -> {
                        BeanUtils.copyProperties(postsDTO, detailsInfo);
                        // 调用subscribe()方法，消费modify订阅
                        detailsService.modify(posts.getId(), detailsInfo).subscribe();
                    }).subscribe()
            ).map(this::convertOuter);
        });
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
