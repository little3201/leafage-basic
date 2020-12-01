/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.api.HypervisorApi;
import top.abeille.basic.assets.bo.UserTidyBO;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.DetailsInfo;
import top.abeille.basic.assets.document.PostsInfo;
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
    private final HypervisorApi hypervisorApi;

    public PostsServiceImpl(PostsRepository postsRepository, DetailsService detailsService,
                            HypervisorApi hypervisorApi) {
        this.postsRepository = postsRepository;
        this.detailsService = detailsService;
        this.hypervisorApi = hypervisorApi;
    }

    @Override
    public Flux<PostsVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return postsRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<DetailsVO> fetchDetailsByCode(String code) {
        Asserts.notBlank(code, "code");
        return postsRepository.findByCodeAndEnabledTrue(code).flatMap(article -> {
                    // 将内容设置到vo对像中
                    DetailsVO detailsVO = new DetailsVO();
                    // 根据业务id获取相关内容
                    return detailsService.fetchByArticleId(article.getId()).map(contentInfo -> {
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
        PostsInfo info = new PostsInfo();
        BeanUtils.copyProperties(postsDTO, info);
        info.setCode(PrefixEnum.PT + this.generateId());
        info.setEnabled(true);
        info.setModifyTime(LocalDateTime.now());
        return postsRepository.insert(info).doOnSuccess(postsInfo -> {
            // 添加内容信息
            DetailsInfo detailsInfo = new DetailsInfo();
            BeanUtils.copyProperties(postsDTO, detailsInfo);
            detailsInfo.setArticleId(postsInfo.getId());
            // 调用subscribe()方法，消费create订阅
            detailsService.create(detailsInfo).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<PostsVO> modify(String code, PostsDTO postsDTO) {
        Asserts.notBlank(code, "code");
        return postsRepository.findByCodeAndEnabledTrue(code).flatMap(info -> {
            // 将信息复制到info
            BeanUtils.copyProperties(postsDTO, info);
            info.setModifyTime(LocalDateTime.now());
            return postsRepository.save(info).doOnSuccess(postsInfo ->
                    // 更新成功后，将内容信息更新
                    detailsService.fetchByArticleId(postsInfo.getId()).doOnNext(detailsInfo -> {
                        BeanUtils.copyProperties(postsDTO, detailsInfo);
                        // 调用subscribe()方法，消费modify订阅
                        detailsService.modify(postsInfo.getId(), detailsInfo).subscribe();
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
    private PostsVO convertOuter(PostsInfo info) {
        PostsVO outer = new PostsVO();
        BeanUtils.copyProperties(info, outer);
        UserTidyBO userTidyBO = hypervisorApi.fetchUser(info.getModifier()).block();
        outer.setAuthor(userTidyBO);
        return outer;
    }

}
